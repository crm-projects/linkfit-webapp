package com.server.storefront.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JWTUtil {

    private JWTUtil() {}

    private static final String SECRET_KEY = "your_secret_key";

    public static String generate(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(null)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }
}
