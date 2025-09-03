package org.kir.controller;

import com.kir.commonservice.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.kir.dto.request.UserRegistrationRequest;
import org.kir.dto.request.ValidateEmailRequest;
import org.kir.dto.response.UserResponse;
import org.kir.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    UserServiceImpl accountService;

    @PostMapping("/public/register")
    public ApiResponse<UserResponse> register(@RequestBody UserRegistrationRequest request){
        log.info("Registering...");
        return ApiResponse.data(accountService.register(request));
    }
    @PostMapping("/public/email/validateAccount")
    public ApiResponse<UserResponse> validateAccount(@RequestBody ValidateEmailRequest request){
        return ApiResponse.data(accountService.validateUser(request));
    }
}
