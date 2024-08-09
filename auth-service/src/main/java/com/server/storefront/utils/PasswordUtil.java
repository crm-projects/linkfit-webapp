package com.server.storefront.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;


public class PasswordUtil {

    public static String encryptPassword(String password) throws NoSuchAlgorithmException {
       return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
