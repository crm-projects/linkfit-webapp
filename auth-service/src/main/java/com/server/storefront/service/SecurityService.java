package com.server.storefront.service;

import com.server.storefront.model.auth.User;
import com.server.storefront.model.auth.UserRegistration;

public interface SecurityService {

    UserRegistration validateAndRegisterUser(UserRegistration userObj);

    User validateAndLoginUser(User user);
}
