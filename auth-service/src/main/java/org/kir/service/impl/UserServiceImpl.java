package org.kir.service.impl;

import com.kir.commonservice.dto.ApiResponse;
import com.kir.commonservice.exception.AppException;
import com.kir.commonservice.exception.ErrorCode;
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
import org.kir.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final RolePermissionRepository rolePermissionRepository;
    private final PassengerClient passengerClient;

    @Value("${app.CHARACTERS}")
    @NonFinal
    String characters;

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

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
//        user.setIsActive(false);

        //Set role
        Role role = roleRepository.findByName(RoleConst.USER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        UserRole userRole = UserRole.builder()
                .role(role)
                .user(user)
                .build();
        user.setUserRoles(Set.of(userRole));

        //Set password
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
                log.info(passengerResponse.getData().toString());
            }
        } catch (AppException e) {
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
//        User user = accountRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
//
//        if(user.getIsActive()){
//            throw new AppException(ErrorCode.ACCOUNT_ACTIVATED);
//        }
//        //log.info("validating");
//        boolean validatedOtp =  notificationClient.validateOtp(request);
//
//        if(!validatedOtp){
//            throw new AppException(ErrorCode.OTP_INVALID);
//        }
//
//        user.setActive(true);
//        accountRepository.save(user);

//        return accountMapper.toAccountResponse(user);
        return null;
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
}
