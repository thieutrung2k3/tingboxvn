package com.kir.commonservice.util;

import java.security.SecureRandom;

public class OtpUtil {

    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1_000_000));
    }

    public static boolean isValidOtp(String otp) {
        return otp.length() == 6 && otp.matches("\\d+");
    }

}
