package com.server.storefront.service;

import com.server.storefront.Profile;
import com.server.storefront.admin.model.Plan;
import com.server.storefront.auth.model.User;
import com.server.storefront.auth.model.UserRegistration;
import com.server.storefront.creator.model.CreatorProfile;
import com.server.storefront.creator.model.SellerPlanMapping;
import com.server.storefront.enums.PlanControl;
import com.server.storefront.plan.repository.PlanRepository;
import com.server.storefront.repository.CreatorRepository;
import com.server.storefront.seller.SellerProfile;
import com.server.storefront.seller.repository.SellerRepository;
import com.server.storefront.utils.ApplicationConstants;
import com.server.storefront.utils.PasswordUtil;
import com.server.storefront.utils.Util;
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
    private PlanRepository planRepository;

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private SellerRepository sellerRepository;

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
            if (type.equalsIgnoreCase(ApplicationConstants.SELLER_PROFILE)) {
                SellerProfile sellerProfile = (SellerProfile) userProfile;
                Plan plan = planRepository.getPlanByName(ApplicationConstants.BASE_PLAN);
                if (Objects.nonNull(plan)) {
                    SellerPlanMapping planMapping = new SellerPlanMapping();
                    planMapping.setPlan(plan);
                    planMapping.setFreeTrial(true);
                    planMapping.setActiveInd(true);
                    planMapping.setExpiresOn(Util.getExpiryPeriod(PlanControl.BASE_PLAN.getExpiryDate()));
                    planMapping.setSellerProfile((SellerProfile) userProfile);
                    sellerProfile.setSellerPlanMapping(planMapping);
                }
                sellerRepository.save(sellerProfile);
            } else {
                creatorRepository.save((CreatorProfile) userProfile);
            }
        } catch (Exception ex) {
            log.error("");
        }
    }
}
