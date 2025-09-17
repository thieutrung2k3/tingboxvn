package org.kir.util;

import com.kir.commonservice.exception.AppException;
import com.kir.commonservice.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Random;

/**
 * Lớp tiện ích cung cấp các phương thức hỗ trợ cho xác thực.
 */
@Slf4j
public class AuthUtil {
    /**
     * Tạo mật khẩu ngẫu nhiên với độ dài 10 ký tự.
     * 
     * Mật khẩu được tạo bao gồm chữ cái in hoa, chữ cái thường, 
     * chữ số và các ký tự đặc biệt như: !@#$%^&*
     * 
     * @return Chuỗi mật khẩu ngẫu nhiên với độ dài 10 ký tự
     */
    public static String generateRandomPassword() {
        // Tập hợp các ký tự có thể sử dụng cho mật khẩu
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        // Tạo mật khẩu có độ dài 10 ký tự
        for (int i = 0; i < 10; i++) {
            // Chọn ngẫu nhiên một ký tự từ tập ký tự cho phép
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public static String generateRandomCode() {
        // Tập hợp các ký tự có thể sử dụng cho mật khẩu
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        // Tạo mật khẩu có độ dài 10 ký tự
        for (int i = 0; i < 10; i++) {
            // Chọn ngẫu nhiên một ký tự từ tập ký tự cho phép
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
    public static String getEmailFromToken() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String email = request.getHeader("X-User-Email");
            if (email != null) {
                return email;
            }
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    public static String getAccountIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            String accountId = jwtAuth.getToken().getClaimAsString("accountId");
            return accountId;
        }
        log.info("Authentication failed: getAccountIdFromToken() - authentication is not JwtAuthenticationToken");
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
}