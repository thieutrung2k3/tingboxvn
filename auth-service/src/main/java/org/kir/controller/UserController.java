package org.kir.controller;

import com.kir.commonservice.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.kir.dto.request.UserRegistrationRequest;
import org.kir.dto.request.ValidateEmailRequest;
import org.kir.dto.response.UserResponse;
import org.kir.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/public/register")
    public ApiResponse<UserResponse> register(@RequestBody UserRegistrationRequest request){
        log.info("[REGISTER]");
        return ApiResponse.data(userService.register(request));
    }

    @PostMapping("/public/email/validateAccount")
    public ApiResponse<UserResponse> validateAccount(@RequestBody ValidateEmailRequest request){
        return ApiResponse.data(userService.validateUser(request));
    }

    @GetMapping("/getInfo")
    public ApiResponse<UserResponse> getUserInfor(){
        return ApiResponse.data(userService.getUserInfo());
    }

    @GetMapping("/admin/getUsersWithPaging")
    public ApiResponse<List<UserResponse>> getUsersWithPaging(Pageable pageable,
                                                              @RequestParam(value = "keyword", required = false) String keyword,
                                                              @RequestParam(value = "from", required = false) LocalDateTime from,
                                                              @RequestParam(value = "to", required = false) LocalDateTime to){
        return ApiResponse.data(userService.getUsersWithPaging(pageable, keyword, from, to));
    }
}
