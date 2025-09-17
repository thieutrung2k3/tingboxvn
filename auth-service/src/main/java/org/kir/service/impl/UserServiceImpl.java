package org.kir.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir.commonservice.cache.QueueMessage;
import com.kir.commonservice.constant.CacheConstant;
import com.kir.commonservice.constant.QueueConstant;
import com.kir.commonservice.dto.ApiResponse;
import com.kir.commonservice.dto.request.CustomerInfo;
import com.kir.commonservice.dto.request.OtpRequest;
import com.kir.commonservice.exception.AppException;
import com.kir.commonservice.exception.ErrorCode;
import com.kir.commonservice.util.OtpUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.kir.constant.RoleConst;
import org.kir.dto.request.UserRegistrationRequest;
import org.kir.dto.request.PassengerCreationRequest;
import org.kir.dto.request.UpdatePasswordRequest;
import org.kir.dto.request.ValidateEmailRequest;
import org.kir.dto.response.UserResponse;
import org.kir.dto.response.PassengerResponse;
import org.kir.entity.Role;
import org.kir.entity.User;
import org.kir.entity.UserRole;
import org.kir.mapper.UserMapper;
import org.kir.repository.UserRepository;
import org.kir.repository.RolePermissionRepository;
import org.kir.repository.RoleRepository;
import org.kir.repository.httpClient.PassengerClient;
import org.kir.service.AuthService;
import org.kir.service.UserService;
import org.kir.service.producer.UserProducer;
import org.kir.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final RolePermissionRepository rolePermissionRepository;
    private final PassengerClient passengerClient;
    private final UserProducer userProducer;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${app.CHARACTERS}")
    @NonFinal
    String characters;

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUserInfo() {
        String email = AuthUtil.getEmailFromToken();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        validateUser(user, false);

        return userMapper.toUserResponse(user);
    }

    /**
     * Register account
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setIsActive(false);

        //Set role
        Role role = roleRepository.findByName(RoleConst.USER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        UserRole userRole = UserRole.builder()
                .role(role)
                .user(user)
                .build();
        user.setUserRoles(Set.of(userRole));
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        //Save to db
        user = userRepository.save(user);

        try {
            PassengerCreationRequest passengerCreationRequest = PassengerCreationRequest.builder()
                    .accountId(user.getId())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phoneNumber(request.getPhoneNumber())
                    .dob(request.getDob())
                    .build();

            ApiResponse<PassengerResponse> passengerResponse = passengerClient.createPassenger(passengerCreationRequest);

            if (!passengerResponse.isSuccess()) {
                throw new AppException(ErrorCode.REGISTRATION_FAILED);
            }
            CustomerInfo customerInfo = CustomerInfo.builder()
                    .setCustomerId(user.getId())
                    .setCustomerName(user.getFirstName() + " " + user.getLastName())
                    .setPhoneNumber(request.getPhoneNumber())
                    .build();
            OtpRequest otpRequest = OtpRequest.builder()
                    .setOtp(OtpUtil.generateOtp())
                    .setEmail(user.getEmail())
                    .setCustomerInfo(customerInfo)
                    .build();
            redisTemplate.opsForValue()
                    .set(CacheConstant.CacheKeys.otpKey(user.getEmail()), otpRequest.getOtp(), 5L, TimeUnit.MINUTES);
            String message = objectMapper.writeValueAsString(otpRequest);

            QueueMessage<OtpRequest> queueMessage = QueueMessage.<OtpRequest>builder()
                    .type(QueueConstant.Type.OTP)
                    .payload(otpRequest)
                    .build();
            userProducer.sendToNotificationQueue(queueMessage);
        } catch (Exception e) {
            throw new AppException(ErrorCode.REGISTRATION_FAILED);
        }

        return userMapper.toUserResponse(user);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request, String accountId) {
        User user = userRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.USER_PASSWORD_ERROR);
        }
    }

    /**
     * @param request
     * @return
     */
    @Override
    public UserResponse validateUser(ValidateEmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        validateUser(user, true);

         String otp = Objects.requireNonNull(redisTemplate.opsForValue().get(request.getEmail())).toString();

        if(!otp.equals(request.getOtp())){
            throw new AppException(ErrorCode.OTP_CODE_INVALID);
        }

        redisTemplate.delete(request.getEmail());
        user.setIsActive(true);
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public String generateRandomUsername(int length) {
        StringBuilder username = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            username.append(characters.charAt(index));
        }
        return username.toString();
    }

    public void validateUser(User user, boolean isActive) {
        if(user == null){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if(user.getIsDelete()){
            throw new AppException(ErrorCode.USER_DELETED);
        }
        if(isActive && user.getIsActive()){
            throw new AppException(ErrorCode.USER_ACTIVATED);
        }
        if(!isActive && !user.getIsActive()){
            throw new AppException(ErrorCode.USER_NOT_ACTIVATED);
        }
    }

    @Override
    public List<UserResponse> getUsersWithPaging(Pageable pageable, String keyword, LocalDateTime from, LocalDateTime to) {
        Page<UserResponse> userResponses = userRepository.getUsersWithPaging(pageable, keyword, from, to);
        return userResponses.getContent();
    }
}
