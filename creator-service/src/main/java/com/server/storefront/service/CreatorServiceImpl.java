package com.server.storefront.service;

import com.server.storefront.model.admin.Plan;
import com.server.storefront.model.creator.Creator;
import com.server.storefront.model.creator.CreatorLite;
import com.server.storefront.model.creator.CreatorPlanMapping;
import com.server.storefront.model.enums.PlanControl;
import com.server.storefront.repository.CreatorRepository;
import com.server.storefront.repository.PlanRepository;
import com.server.storefront.utils.ApplicationConstants;
import com.server.storefront.utils.EmailConstants;
import com.server.storefront.utils.OTPGenerator;
import com.server.storefront.utils.Util;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.utils.exception.RandomGeneratorException;
import com.server.storefront.utils.helper.EmailSender;
import com.server.storefront.model.enums.EmailDomain;
import com.server.storefront.utils.holder.OTPHolder;
import com.server.storefront.model.auth.SignIn;
import com.server.storefront.model.auth.SignUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;
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
    public SignUp creatorSignUp(SignUp authObj) throws CreatorException, RandomGeneratorException {
        if (authObj == null) {
            throw new CreatorException("Invalid Login Details");
        }
        boolean postProcess = (authObj.isHasOTPInput() && authObj.isHasAuthToken());
        return (postProcess) ? validateAndSaveCreator(authObj) : prepareEmailAuthentication(authObj);
    }

    private SignUp prepareEmailAuthentication(SignUp authObj) throws RandomGeneratorException {
        boolean isValid = isValidEmailDomain(authObj.getUserEmail());
        if (isValid) {
            logger.info("Valid Email Entered By User :{} ", authObj.getUserEmail());

            String OTP = OTPGenerator.generateOTP(authObj.getUserEmail());
            otpHolder.updateEmailOTPMap(authObj.getUserEmail(), OTP);
            prepareAuthenticationMail(authObj.getUserEmail(), OTP);
            authObj.setHasAuthToken(true);
        }
        return authObj;
    }

    @Transactional
    private SignUp validateAndSaveCreator(SignUp authObj) {
        boolean isAuth = false;
        String generatedOTP = otpHolder.getOTPByEmailAddress(authObj.getUserEmail());
        isAuth = generatedOTP.equalsIgnoreCase(authObj.getInputOTP());
        if (isAuth) {
            logger.info("OTP Successfully Entered By User :{} ", authObj.getUserEmail());
            Creator creator = scrubAndSaveCreatorItem(authObj);
            authObj.setCreatorId(creator.getId());
        } else {
            logger.info("Invalid OTP Entered By User :{}", authObj.getUserEmail());
        }
        return authObj;
    }

    @Override
    public Creator creatorSignIn(SignIn authObj) {
        return validateCreateSignIn(authObj).get();
    }

    private Optional<Creator> validateCreateSignIn(SignIn authObj) {
        return creatorRepository.findCreatorByEmailAddress(authObj.getUserEmail());
    }

    @Override
    @Transactional
    public Creator saveCreatorProfile(CreatorLite creatorLite) throws CreatorException {
        try {
            if (creatorLite == null) {
                throw new RuntimeException("Null Object is Passed");
            }
            boolean isExistingCreator = StringUtils.hasText(creatorLite.getCreatorId());
            Creator creator = (isExistingCreator) ?
                    creatorRepository.findById(creatorLite.getCreatorId()).get() : new Creator();
            scrubCreatorProfileItems(creator, creatorLite);
            return saveUpdateCreatorProfileSettings(creator, isExistingCreator);
        } catch (Exception e) {
            throw new CreatorException(e.getMessage());
        }
    }

    private void scrubCreatorProfileItems(Creator creator, CreatorLite creatorLite) {
        creator.setDescription(creator.getDescription());
        creator.setUserName(creatorLite.getUserName());
        creator.setDateOfBirth(creatorLite.getDateOfBirth());
        creator.setDisplayPicURL(creatorLite.getDisplayPicURL());
        creator.setGender(creatorLite.getGender());

        if (StringUtils.hasText(creatorLite.getPlan().getPlanId())) {
            CreatorPlanMapping planMapping = new CreatorPlanMapping();
            Optional<Plan> plan = planRepository.findById(creatorLite.getPlan().getPlanId());
            if (plan.isPresent()) {
                planMapping.setPlan(plan.get());
                planMapping.setFreeTrial(creatorLite.getPlan().isFreeTrial());
                planMapping.setActiveInd(creatorLite.getPlan().isFreeTrial());
                planMapping.setCreator(creator);
            }
            creator.setCreatorPlanMapping(planMapping);
        }
    }

    @Transactional
    public Creator saveUpdateCreatorProfileSettings(Creator creator, boolean isExistingCreator) {

        if (isExistingCreator) {
            Creator existingCreator = creatorRepository.findById(creator.getId()).get();;
            existingCreator.setEmailAddress(creator.getEmailAddress());
            existingCreator.setGender(creator.getGender());
            existingCreator.setDateOfBirth(creator.getDateOfBirth());
            existingCreator.setDisplayPicURL(creator.getDisplayPicURL());
            existingCreator.setDescription(creator.getDescription());
            existingCreator.setActiveInd(true);
            existingCreator.setCreatorPlanMapping(creator.getCreatorPlanMapping());

            creatorRepository.save(existingCreator);
            logger.info("Successfully Updated Creator Profile Settings for id :{}", creator.getId());
            return existingCreator;
        } else {
            creator.setEmailAddress(creator.getEmailAddress());
            creator.setGender(creator.getGender());
            creator.setDateOfBirth(creator.getDateOfBirth());
            creator.setDisplayPicURL(creator.getDisplayPicURL());
            creator.setDescription(creator.getDescription());
            creator.setCreatorPlanMapping(creator.getCreatorPlanMapping());
            creator.setActiveInd(true);

            creatorRepository.save(creator);
            logger.info("Successfully Saved Creator Profile Settings for id :{}", creator.getId());
            return creator;
        }
    }

    @Transactional
    private Creator scrubAndSaveCreatorItem(SignUp authObj) {
        Creator creator = new Creator();
        creator.setId(UUID.randomUUID().toString());
        creator.setUserName(authObj.getUserName());
        creator.setEmailAddress(authObj.getUserEmail());
        creator.setDateOfBirth(authObj.getDateOfBirth());
        creator.setActiveInd(true);
        CreatorPlanMapping creatorPlanMapping = new CreatorPlanMapping();
        Plan planObj = planRepository.getPlanByName(ApplicationConstants.BASE_PLAN);
        if (planObj != null) {
            int expiryDate = PlanControl.valueOf(ApplicationConstants.BASE_PLAN).getExpiryDate();
            validateCreatorPlanDetails(planObj, creator, true, true, expiryDate);
        }
        logger.info("Saving Creator Object with Id: {} ", creator.getId());
        return creatorRepository.save(creator);
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

    @Override
    @Transactional
    public Creator deleteCreatorProfile(CreatorLite creatorLite) {
        if (!StringUtils.hasText(creatorLite.getCreatorId())) {
            throw new RuntimeException("Placeholder Error Message");
        }

        Creator existingCreator = null;
        existingCreator = creatorRepository.findById(creatorLite.getCreatorId()).get();
        existingCreator.setActiveInd(false);
        creatorRepository.save(existingCreator);

        logger.info("Deleted Creator Profile for id :{}", existingCreator.getId());
        return existingCreator;
    }

}

