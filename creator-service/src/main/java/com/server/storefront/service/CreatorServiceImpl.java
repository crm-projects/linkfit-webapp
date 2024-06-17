package com.server.storefront.service;

import com.server.storefront.admin.Plan;
import com.server.storefront.creator.Creator;
import com.server.storefront.creator.CreatorLite;
import com.server.storefront.creator.CreatorPlanMapping;
import com.server.storefront.enums.PlanControl;
import com.server.storefront.repository.CreatorRepository;
import com.server.storefront.repository.PlanRepository;
import com.server.storefront.utils.ApplicationConstants;
import com.server.storefront.utils.EmailConstants;
import com.server.storefront.utils.OTPGenerator;
import com.server.storefront.utils.Util;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.utils.helper.EmailSender;
import com.server.storefront.enums.EmailDomain;
import com.server.storefront.utils.holder.OTPHolder;
import com.server.storefront.utils.holder.SignUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.UUID;

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
    public Creator saveUpdateCreatorProfileSettings(CreatorLite creatorLite, boolean isExistingCreator) throws CreatorException {
        try {
            if (creatorLite == null) {
                throw new CreatorException("Creator Object is Null");
            }
            Creator creator = (isExistingCreator) ? creatorRepository.findById(creatorLite.getCreatorId()).get() : new Creator();

            creator.setUserName(creatorLite.getUserName());
            creator.setEmailAddress(creatorLite.getEmailAddress());
            creator.setGender(creatorLite.getGender());
            creator.setDateOfBirth(creatorLite.getDateOfBirth());
            creator.setDisplayPicURL(creatorLite.getDisplayPicURL());
            creator.setDescription(creatorLite.getDescription());

            Plan plan = new Plan();
            if (creatorLite.getPlan() != null && StringUtils.hasText(creatorLite.getPlan().getPlanId())) {
                int expiryDate = PlanControl.valueOf(ApplicationConstants.BASE_PLAN).getExpiryDate();
                plan = planRepository.findById(creatorLite.getPlan().getPlanId()).get();
                validateCreatorPlanDetails(plan, creator, creatorLite.getPlan().isActiveInd(), creatorLite.getPlan().isFreeTrial(), expiryDate);
            }
            return creatorRepository.save(creator);
        } catch (Exception e) {
            throw new CreatorException(e.getMessage());
        }
    }

    @Override
    public SignUp authenticateCreator(SignUp authObj) throws CreatorException {
        boolean isAuth = false;
        try {
            if (authObj == null) {
                logger.error("Input Object is Null");
                throw new CreatorException("Invalid Login Details");
            }
            if (authObj.isHasOTPInput() && authObj.isHasAuthToken()) {
                String generatedOTP = otpHolder.getOTPByEmailAddress(authObj.getUserEmail());
                isAuth = generatedOTP.equalsIgnoreCase(authObj.getInputOTP());
                if (isAuth) {
                    logger.info("Processing Creator Object for: {} ", authObj.getUserEmail());
                    scrubAndSaveCreatorItem(authObj);
                }
            } else {
                boolean isValid = isValidEmailDomain(authObj.getUserEmail());
                if (isValid) {
                    String OTP = OTPGenerator.generateOTP(authObj.getUserEmail());
                    otpHolder.updateEmailOTPMap(authObj.getUserEmail(), OTP);
                    prepareAuthenticationMail(authObj.getUserEmail(), OTP);
                    authObj.setHasAuthToken(true);
                }
            }
        } catch (Exception e) {
            throw new CreatorException(e.getMessage());
        }
        return authObj;
    }

    @Transactional
    private void scrubAndSaveCreatorItem(SignUp authObj) {
        Creator creator = new Creator();
        creator.setId(UUID.randomUUID().toString());
        creator.setUserName(authObj.getUserName());
        creator.setEmailAddress(authObj.getUserEmail());
        creator.setDateOfBirth(authObj.getDateOfBirth());

        CreatorPlanMapping creatorPlanMapping = new CreatorPlanMapping();
        Plan planObj = planRepository.getPlanByName(ApplicationConstants.BASE_PLAN);
        if (planObj != null) {
            int expiryDate = PlanControl.valueOf(ApplicationConstants.BASE_PLAN).getExpiryDate();
            validateCreatorPlanDetails(planObj, creator, true, true, expiryDate);
        }
        logger.info("Saving Creator Object with Id: {} ", creator.getId());
        creatorRepository.save(creator);
    }

    private void prepareAuthenticationMail(String userEmail, String otp) {
        logger.info("Preparing Email Message for OTP Verification to User: {} ", userEmail);
        String subject = EmailConstants.OTP_AUTH_SUBJECT;
        emailSender.sendEmailNotification(userEmail, subject, otp);
    }

    private boolean isValidEmailDomain(String userEmail) {
        logger.info("Email Domain Validation for Authentication");
        int fromIndex = userEmail.indexOf("@");
        int endIndex = userEmail.length();
        String inputDomain = userEmail.substring(fromIndex, endIndex);
        return Arrays.stream(EmailDomain.values()).anyMatch(domain -> domain.getDomain().equalsIgnoreCase(inputDomain));
    }

    private void validateCreatorPlanDetails(Plan plan, Creator creator, boolean isActiveInd, boolean isFreeTrial, int expiryDate) {
        if (plan != null) {
            CreatorPlanMapping creatorPlanMapping = new CreatorPlanMapping();
            creatorPlanMapping.setPlan(plan);
            creatorPlanMapping.setCreator(creator);
            creatorPlanMapping.setActiveInd(isActiveInd);
            creatorPlanMapping.setFreeTrial(isFreeTrial);
            creatorPlanMapping.setLocalDateTime(Util.getExpiryPeriod(expiryDate));
            creator.setCreatorPlanMapping(creatorPlanMapping);
        }
    }
}

