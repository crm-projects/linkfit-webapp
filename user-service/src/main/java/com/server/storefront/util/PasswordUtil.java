package com.server.storefront.util;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

@Slf4j
public class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String inputPassword, String hashPassword) {
        return BCrypt.checkpw(inputPassword, hashPassword);
    }

}
