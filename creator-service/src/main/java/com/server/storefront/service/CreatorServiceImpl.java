package com.server.storefront.service;

import com.server.storefront.model.*;
import com.server.storefront.repository.CreatorRepository;
import com.server.storefront.repository.PlanRepository;
import com.server.storefront.repository.PlatformRepository;
import com.server.storefront.utils.ApplicationConstants;
import com.server.storefront.utils.EmailConstants;
import com.server.storefront.utils.OTPGenerator;
import com.server.storefront.utils.Util;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.utils.helper.EmailSender;
import com.server.storefront.utils.holder.EmailDomain;
import com.server.storefront.utils.holder.OTPHolder;
import com.server.storefront.utils.holder.SignUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.server.storefront.utils.EmailConstants.OTP_AUTH_SUBJECT;

@Service
public class CreatorServiceImpl implements CreatorService {

    private static final Logger logger = LoggerFactory.getLogger(CreatorServiceImpl.class);

    @Autowired
    CreatorRepository creatorRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    OTPHolder otpHolder;

    @Autowired
    EmailSender emailSender;

    @Override
    @Transactional
    public Creator saveUpdateCreatorProfileSettings(CreatorLite creatorLite) throws CreatorException {
        try {
            if (creatorLite == null) {
                throw new CreatorException("Creator Object is Null");
            }
            Creator creator = new Creator();
            if (creatorLite.isExistingCreator()) {
                Optional<Creator> existingCreatorObj = creatorRepository.findById(creatorLite.getCreatorId());
                if (existingCreatorObj.isPresent()) {
                    existingCreatorObj.get().setUserName(creatorLite.getUserName());
                    existingCreatorObj.get().setEmailAddress(creatorLite.getEmailAddress());
                    existingCreatorObj.get().setGender(creatorLite.getGender());
                    validateCreatorPlanDetails(creatorLite.getPlan(), existingCreatorObj.get());
                    creator = existingCreatorObj.get();
                }
            } else {
                Creator creatorObj = new Creator();
                creatorObj.setUserName(creatorLite.getUserName());
                creatorObj.setEmailAddress(creatorLite.getEmailAddress());
                creatorObj.setGender(creatorLite.getGender());
                validateCreatorPlanDetails(creatorLite.getPlan(), creatorObj);
                creator = creatorObj;
            }
            return creatorRepository.save(creator);
        } catch (Exception e) {
            throw new CreatorException(e.getMessage());
        }
    }

    @Override
    public SignUp authenticateCreator(SignUp signupObj) throws CreatorException {
        boolean isAuth = false;
        try {
            if (signupObj == null) {
                logger.error("Input Object is Null");
                throw new CreatorException("Invalid Login Details");
            }
            if (signupObj.isHasOTPInput() && signupObj.isHasAuthToken()) {
                String generatedOTP = otpHolder.getOTPByEmailAddress(signupObj.getUserEmail());
                isAuth = generatedOTP.equalsIgnoreCase(signupObj.getInputOTP());
                if (isAuth) {
                    logger.info("Processing Creator Object for: {} ", signupObj.getUserEmail());
                    scrubAndSaveCreatorItem(signupObj, isAuth);
                }
            } else {
                boolean isValid = isValidEmailDomain(signupObj.getUserEmail());
                if (isValid) {
                    String OTP = OTPGenerator.generateOTP(signupObj.getUserEmail());
                    otpHolder.updateEmailOTPMap(signupObj.getUserEmail(), OTP);
                    prepareAuthenticationMail(signupObj.getUserEmail(), OTP);
                    signupObj.setHasAuthToken(true);
                }
            }
        } catch (Exception e) {
            throw new CreatorException(e.getMessage());
        }
        return signupObj;
    }

    @Transactional
    private void scrubAndSaveCreatorItem(SignUp signupObj, boolean isAuth) {
        Creator creator = new Creator();
        creator.setId(UUID.randomUUID().toString());
        creator.setUserName(signupObj.getUserName());
        creator.setEmailAddress(signupObj.getUserEmail());
        creator.setDateOfBirth(signupObj.getDateOfBirth());

        if (isAuth) {
            CreatorPlanMapping creatorPlanMapping = new CreatorPlanMapping();
            Plan planObj = planRepository.getPlanByName(ApplicationConstants.BASE_PLAN);
            if (planObj != null) {
                creatorPlanMapping.setPlan(planObj);
                creatorPlanMapping.setFreeTrial(true);
                creatorPlanMapping.setActiveInd(true);
                creatorPlanMapping.setLocalDateTime(Util.getExpiryPeriod(7)); //To change based on Free plan limit.
                creatorPlanMapping.setCreator(creator);
            }
        }
        logger.info("Saving Creator Object with Id: {} ", creator.getId());
        creatorRepository.save(creator);
    }

    private void prepareAuthenticationMail(String userEmail, String otp) {
        logger.info("Preparing Email Message for OTP Verification to User: {} ", userEmail);
        String subject = EmailConstants.OTP_AUTH_SUBJECT;
        emailSender.sendEmailNotification(userEmail,subject,otp);
    }

    private boolean isValidEmailDomain(String userEmail) {
        logger.info("Email Domain Validation for Authentication");
        int fromIndex = userEmail.indexOf("@");
        int endIndex = userEmail.length();
        String inputDomain = userEmail.substring(fromIndex, endIndex);
        return Arrays.stream(EmailDomain.values()).anyMatch(domain -> domain.getDomain().equalsIgnoreCase(inputDomain));
    }

    private void validateCreatorPlanDetails(PlanLite planLiteObj, Creator creatorObj) {
        if (planLiteObj != null) {
            if (StringUtils.hasLength(planLiteObj.getPlanId())) {
                Plan planObj = planRepository.findById(planLiteObj.getPlanId()).get();
                CreatorPlanMapping creatorPlanMapping = new CreatorPlanMapping();
                creatorPlanMapping.setPlan(planObj);
                creatorPlanMapping.setCreator(creatorObj);
                creatorPlanMapping.setActiveInd(planLiteObj.isActiveInd());
                creatorPlanMapping.setFreeTrial(planLiteObj.isFreeTrial());
                creatorObj.setCreatorPlanMapping(creatorPlanMapping);
            }
        }
    }
}
