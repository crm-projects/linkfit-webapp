package com.server.storefront.helper;

import com.server.storefront.exception.RandomGeneratorException;

import java.security.SecureRandom;

public class OTPGenerator {

    private static final int OTP_LENGTH = 6;
    private static final String OTP_CHARS = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static <T> String generateOTP(T input) throws RandomGeneratorException {
        try {
            StringBuilder OTP = new StringBuilder();
            for (int i = 0; i < OTP_LENGTH; i++) {
                OTP.append(OTP_CHARS.charAt(random.nextInt(OTP_CHARS.length())));
            }
            return OTP.toString();
        } catch (Exception ex) {
            throw new RandomGeneratorException(ex.getMessage());
        }
    }
}
