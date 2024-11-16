package com.server.storefront.util;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
public class OTPUtil {

    private OTPUtil() {}

    private static final int OTP_LENGTH = 6;
    private static final String OTP_CHARS = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateOTP() {
        try {
            StringBuilder otp = new StringBuilder();
            for (int i = 0; i < OTP_LENGTH; i++) {
                otp.append(OTP_CHARS.charAt(random.nextInt(OTP_CHARS.length())));
            }
            return otp.toString();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
}
