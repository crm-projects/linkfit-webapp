package com.server.storefront.service;

import com.server.storefront.dto.User;
import com.server.storefront.dto.UserRegistration;
import jakarta.servlet.http.HttpServletRequest;

public interface SecurityService {

    String validateAndRegisterUser(UserRegistration userObj, HttpServletRequest request);

    User validateAndLoginUser(User user);
}
