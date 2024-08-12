package com.server.storefront.service;


import com.server.storefront.dto.User;
import com.server.storefront.dto.UserRegistration;
import com.server.storefront.utils.PasswordUtil;
import com.server.storefront.repository.CreatorRepository;
import com.server.storefront.model.CreatorProfile;
import com.server.storefront.model.Profile;
import com.server.storefront.constants.ApplicationConstants;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private CreatorRepository creatorRepository;

    @Override
    @Transactional
    public String validateAndRegisterUser(UserRegistration user, HttpServletRequest request) {
        if (Objects.isNull(user)) {
            log.error("");
        }
        String userType = request.getHeader(ApplicationConstants.STOREFRONT_USER);
        Profile userProfile = (Profile) request.getAttribute(ApplicationConstants.USER_PROFILE);
        return authenticateUserDetails(user, userProfile, userType);
    }

    @Override
    @Transactional(readOnly = true)
    public User validateAndLoginUser(User user) {
        if (Objects.isNull(user)) {
            log.error("");
        }
        return authenticateAndLoginUser(user);
    }

    private User authenticateAndLoginUser(User user) {
        return null;
    }


    private String authenticateUserDetails(UserRegistration user, Profile userProfile, String type) {
        try {
            userProfile.setUserName(user.getUserName());
            userProfile.setEmailAddress(user.getUserEmail());
            userProfile.setPassword(PasswordUtil.encryptPassword(user.getUserPassword()));
            validatePlanAndRegisterUser(userProfile, type);
            return userProfile.getId();
        } catch (Exception ex) {
            log.error("");
            throw new RuntimeException();
        }
    }

    private void validatePlanAndRegisterUser(Profile userProfile, String type) {
        try {
            if (type.equalsIgnoreCase(ApplicationConstants.CREATOR_PROFILE)) {
                creatorRepository.save((CreatorProfile) userProfile);
            }
        } catch (Exception ex) {
            log.error("");
        }
    }
}
