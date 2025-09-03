package org.kir.service;

import com.nimbusds.jwt.SignedJWT;
import org.kir.dto.request.LoginRequest;
import org.kir.dto.request.LogoutRequest;
import org.kir.dto.request.RefreshRequest;
import org.kir.dto.request.ValidateTokenRequest;
import org.kir.dto.response.LoginResponse;
import org.kir.dto.response.RefreshResponse;
import org.kir.dto.response.ValidateTokenResponse;
import org.kir.entity.User;

import java.util.Set;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    void logout(LogoutRequest request);

    RefreshResponse refreshToken(RefreshRequest request);

    ValidateTokenResponse validateToken(ValidateTokenRequest request);

    SignedJWT verifyToken(String token, boolean isRefresh);

    String generateToken(User user);

    String buildScope(User user);
}
