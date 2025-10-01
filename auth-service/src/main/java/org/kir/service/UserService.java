package org.kir.service;

import org.kir.dto.request.UserRegistrationRequest;
import org.kir.dto.request.UpdatePasswordRequest;
import org.kir.dto.request.ValidateEmailRequest;
import org.kir.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    UserResponse register(UserRegistrationRequest request);

    void updatePassword(UpdatePasswordRequest request, Long userId);

    UserResponse validateUser(ValidateEmailRequest request);

    String generateRandomUsername(int length);

    UserResponse getUserInfo();

    List<UserResponse> getUsersWithPaging(Pageable pageable, String keyword, Boolean status, LocalDateTime from, LocalDateTime to);

    void updateUserStatus(Long userId, Boolean status);
}
