package com.server.storefront.service;

import com.server.storefront.dto.Credentials;

import java.util.Map;

public interface StartupService {

    boolean generateOtpByEmail(Credentials credentials);

    boolean validateUsername(String userName);

    Map<String, Object> processUserRegistration(Map<String, String> userDetails, String profile);

    String authorizeUserLogin(Credentials credentials);
}
