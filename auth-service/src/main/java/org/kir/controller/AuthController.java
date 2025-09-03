package org.kir.controller;

import com.kir.commonservice.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.kir.dto.request.*;
import org.kir.dto.response.LoginResponse;
import org.kir.dto.response.RefreshResponse;
import org.kir.dto.response.ValidateTokenResponse;
import org.kir.service.impl.AuthServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthServiceImpl authService;

    @PostMapping("/public/email/sendOtp")
    public ApiResponse<String> sendOtp(@RequestBody String email){
        authService.sendOtp(email);
        return ApiResponse.data("Send OTP successfully.");
    }

    @PostMapping("/testLogin")
    public ApiResponse<String> testLogin(){
        return ApiResponse.data("Test");
    }

    @PostMapping("/public/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request){
        return ApiResponse.data(authService.login(request));
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LogoutRequest request){
        authService.logout(request);
        return ApiResponse.data("Logout successfully");
    }

    @PostMapping("/public/token/validate")
    public ApiResponse<ValidateTokenResponse> validateToken(@RequestBody ValidateTokenRequest request){
        log.info("Processing request for validateToken: {}", request.getToken());
        return ApiResponse.data(authService.validateToken(request));
    }

    @PostMapping("/public/refreshToken")
    public ApiResponse<RefreshResponse> refreshToken(@RequestBody RefreshRequest request){
        return ApiResponse.data(authService.refreshToken(request));
    }
}
