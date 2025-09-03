package org.kir.service;

import org.kir.dto.request.UserRegistrationRequest;
import org.kir.dto.request.UpdatePasswordRequest;
import org.kir.dto.request.ValidateEmailRequest;
import org.kir.dto.response.UserResponse;

public interface UserService {
    UserResponse register(UserRegistrationRequest request);

    void updatePassword(UpdatePasswordRequest request, String accountId);

    UserResponse validateUser(ValidateEmailRequest request);

    String generateRandomUsername(int length);
}
