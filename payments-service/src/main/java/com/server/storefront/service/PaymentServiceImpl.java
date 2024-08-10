package com.server.storefront.service;

import com.server.storefront.utils.constants.ApplicationConstants;
import com.server.storefront.utils.model.CreatorProfile;
import com.server.storefront.utils.model.UserBankDetails;
import com.server.storefront.utils.repository.CreatorRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private CreatorRepository creatorRepository;

    @Override
    @Transactional
    public UserBankDetails getUserPaymentDetails(UserBankDetails userBankDetails, String userId, HttpServletRequest request) {
        try {
            String type = request.getHeader(ApplicationConstants.STOREFRONT_USER);
            if (validateBankAccountNumber(userBankDetails.getAccountNumber()) &&
                    validateBankIFSCCode(userBankDetails.getBankIFSCCode()) &&
                    validateUserPANDetails(userBankDetails.getPermanentAccountNumber())) {
                saveUserPaymentDetails(userBankDetails, type, userId);
            }
            return new UserBankDetails();
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    private boolean validateUserPANDetails(String permanentAccountNumber) {
        if (!StringUtils.hasText(permanentAccountNumber)) {
            log.error("");
        }
        return permanentAccountNumber.matches(ApplicationConstants.PAN_VALIDATION);
    }

    private boolean validateBankIFSCCode(String bankIFSCCode) {
        if (!StringUtils.hasText(bankIFSCCode)) {
            log.error("");
        }
        return bankIFSCCode.matches(ApplicationConstants.IFSC_VALIDATION);
    }

    private boolean validateBankAccountNumber(String accountNumber) {
        if (!StringUtils.hasText(accountNumber)) {
            log.error("");
        }

        return accountNumber.matches(ApplicationConstants.ACCOUNT_NUMBER_VALIDATION);
    }

    private void saveUserPaymentDetails(UserBankDetails userBankDetails, String type, String userId) {

        switch (type.toUpperCase()) {
            case ApplicationConstants.CREATOR_PROFILE -> {
                CreatorProfile creatorProfile = creatorRepository.findById(userId).orElse(null);
                if (Objects.nonNull(creatorProfile)) {
                    creatorProfile.setCreatorBankDetails(userBankDetails);
                    creatorRepository.save(creatorProfile);
                }

            }
            default -> {
            }
        }
    }

}
