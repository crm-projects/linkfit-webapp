package com.server.storefront.utils;

import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    public static String encryptPassword(String password) throws NoSuchAlgorithmException {
        return "";
//        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}