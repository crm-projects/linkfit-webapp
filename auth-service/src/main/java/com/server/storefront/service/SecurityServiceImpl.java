package com.server.storefront.service;

import com.server.storefront.model.auth.User;
import com.server.storefront.model.auth.UserRegistration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    @Transactional
    public UserRegistration validateAndRegisterUser(UserRegistration user) {
        if (Objects.isNull(user)) {
            log.error("");
        }
        return authenticateAndCreateUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User validateAndLoginUser(User user) {
        if ( Objects.isNull(user)) {
            log.error("");
        }
        return authenticateAndLoginUser(user);
    }

    private User authenticateAndLoginUser(User user) {
        return null;
    }


    private UserRegistration authenticateAndCreateUser(UserRegistration user) {
        return null;
    }
}
