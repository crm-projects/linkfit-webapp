package com.server.storefront.service;


import com.server.storefront.model.UserBankDetails;
import com.server.storefront.model.CreatorProfile;
import com.server.storefront.repository.CreatorRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final CreatorRepository creatorRepository;

    public static final String PAN_VALIDATION = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
    public static final String IFSC_VALIDATION = "^[A-Z]{4}0[A-Z0-9]{6}$";
    public static final String ACCOUNT_NUMBER_VALIDATION = "^[0-9]{9,18}$";
    public static final String CREATOR_PROFILE = "CREATOR";
    public static final String STOREFRONT_USER = "X-STOREFRONT-USER";

    @Override
    @Transactional
    public UserBankDetails getUserPaymentDetails(UserBankDetails userBankDetails, String userId, HttpServletRequest request) {
        try {
            String type = request.getHeader(STOREFRONT_USER);
            if (validateBankAccountNumber(userBankDetails.getAccountNumber()) &&
                    validateBankIFSCCode(userBankDetails.getBankIFSCCode()) &&
                    validateUserPANDetails(userBankDetails.getPermanentAccountNumber())) {
                saveUserPaymentDetails(userBankDetails, type, userId);
            }
            return new UserBankDetails();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private boolean validateUserPANDetails(String permanentAccountNumber) {
        if (!StringUtils.hasText(permanentAccountNumber)) {
            log.error("");
        }
        return permanentAccountNumber.matches(PAN_VALIDATION);
    }

    private boolean validateBankIFSCCode(String bankIFSCCode) {
        if (!StringUtils.hasText(bankIFSCCode)) {
            log.error("");
        }
        return bankIFSCCode.matches(IFSC_VALIDATION);
    }

    private boolean validateBankAccountNumber(String accountNumber) {
        if (!StringUtils.hasText(accountNumber)) {
            log.error("");
        }

        return accountNumber.matches(ACCOUNT_NUMBER_VALIDATION);
    }

    private void saveUserPaymentDetails(UserBankDetails userBankDetails, String type, String userId) {

        switch (type.toUpperCase()) {
            case CREATOR_PROFILE -> {
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
