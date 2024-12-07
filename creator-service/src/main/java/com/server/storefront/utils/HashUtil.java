package com.server.storefront.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtil {

    private static final String SHA256 = "SHA-256";

    public static String hashIP(String ipAddress) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA256);
            byte[] hash = messageDigest.digest(ipAddress.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
