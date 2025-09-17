package org.kir.service.impl;

import com.kir.commonservice.exception.AppException;
import com.kir.commonservice.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.kir.dto.request.LoginRequest;
import org.kir.dto.request.LogoutRequest;
import org.kir.dto.request.RefreshRequest;
import org.kir.dto.request.ValidateTokenRequest;
import org.kir.dto.response.LoginResponse;
import org.kir.dto.response.RefreshResponse;
import org.kir.dto.response.ValidateTokenResponse;
import org.kir.entity.User;
import org.kir.repository.UserRepository;
import org.kir.repository.RolePermissionRepository;
import org.kir.service.AuthService;
import org.kir.service.UserPermissionService;
import org.kir.service.cache.TokenBlacklistCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;

    private final UserPermissionService userPermissionService;
    private final TokenBlacklistCacheService tokenBlacklistCacheService;

    private final PasswordEncoder passwordEncoder;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.valid-duration}")
    @NonFinal
    private int VALID_DURATION;

    @Value("${jwt.refresh-duration}")
    @NonFinal
    private int REFRESH_DURATION;


    @Value("${jwt.private-key}")
    @NonFinal
    private String PRIVATE_KEY;

    public void sendOtp(String email) {
        log.info("sendOTP");
        kafkaTemplate.send("sendOtp-v1", email);
    }

    /**
     * Handle user login logic
     *
     * @param request (email, username, password)
     * @return LoginResponse(token)
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(user.getIsDelete()){
            throw new AppException(ErrorCode.USER_DELETED);
        }

        if(!user.getIsActive()){
            throw new AppException(ErrorCode.USER_NOT_ACTIVATED);
        }

        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!isAuthenticated)
            throw new AppException(ErrorCode.USER_PASSWORD_ERROR);

        String token = generateToken(user);
        return LoginResponse.builder()
                .token(token)
                .build();

    }

    @Override
    public void logout(LogoutRequest request) {
        try {
            SignedJWT signedJWT = verifyToken(request.getToken(), true);

            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
//            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            tokenBlacklistCacheService.cacheBlacklistToken(jwtId);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    public RefreshResponse refreshToken(RefreshRequest request) {
        try {
            String token = request.getToken();
            //Verify token
            SignedJWT signedJWT = verifyToken(token, true);

            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            tokenBlacklistCacheService.cacheBlacklistToken(jwtId);

            String email = signedJWT.getJWTClaimsSet().getStringClaim("email");
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            token = generateToken(user);

            return RefreshResponse.builder()
                    .token(token)
                    .build();

        } catch (ParseException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

    }

    @Override
    public ValidateTokenResponse validateToken(ValidateTokenRequest request) {
        String token = request.getToken();
        try {
            //Verify token
            SignedJWT signedJWT = verifyToken(token, false);
            log.info("Request true.");
            return ValidateTokenResponse.builder()
                    .validate(true)
                    .userId(signedJWT.getJWTClaimsSet().getSubject())
                    .email(signedJWT.getJWTClaimsSet().getStringClaim("email"))
                    .scopes(signedJWT.getJWTClaimsSet().getStringClaim("scope"))
                    .build();
        } catch (Exception e) {
            log.info("Request false.");
            return ValidateTokenResponse.builder()
                    .validate(false)
                    .build();
        }
    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefresh) {
        try {
            JWSVerifier jwsVerifier = new MACVerifier(PRIVATE_KEY);
            SignedJWT signedJWT = SignedJWT.parse(token);

            boolean verified = signedJWT.verify(jwsVerifier);

            if (!verified || tokenBlacklistCacheService.isBlacklisted(signedJWT.getJWTClaimsSet().getJWTID()))
                throw new AppException(ErrorCode.UNAUTHENTICATED);

            Date expiryTime = isRefresh ?
                    new Date(signedJWT
                            .getJWTClaimsSet()
                            .getIssueTime()
                            .toInstant()
                            .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                            .toEpochMilli())
                    : signedJWT.getJWTClaimsSet().getExpirationTime();

            if (expiryTime.before(new Date())) {
                tokenBlacklistCacheService.cacheBlacklistToken(signedJWT.getJWTClaimsSet().getJWTID());
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            return signedJWT;
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .issuer("devKir")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("email", user.getEmail())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(PRIVATE_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public String buildScope(User user) {
        StringBuilder scope = new StringBuilder();
        Set<String> effectivePermissions = userPermissionService.getEffectivePermissions(user);
        Set<String> userRoles = userPermissionService.getUserRoles(user);

        scope.append(String.join(" ", effectivePermissions));

        if (!userRoles.isEmpty()) {
            scope.append(" ")
                    .append(userRoles.stream()
                            .map(role -> "ROLE_" + role)
                            .collect(Collectors.joining(" ")));
        }

        return scope.toString();
    }
}
