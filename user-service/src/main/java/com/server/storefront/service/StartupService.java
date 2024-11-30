package com.server.storefront.service;

import com.server.storefront.dto.Credentials;
import com.server.storefront.exception.StartupException;

import java.util.Map;

public interface StartupService {

    boolean generateOtpByEmail(Credentials credentials) throws StartupException;

    boolean validateUsername(String userName);

    Map<String, Object> processUserRegistration(Map<String, String> userDetails, String profile) throws StartupException;

    String authorizeUserLogin(Credentials credentials);
}
