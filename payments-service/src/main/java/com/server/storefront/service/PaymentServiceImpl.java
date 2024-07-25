package com.server.storefront.service;

import com.server.storefront.Profile;
import com.server.storefront.auth.model.UserBankDetails;
import com.server.storefront.creator.model.CreatorProfile;
import com.server.storefront.repository.CreatorRepository;
import com.server.storefront.utils.ApplicationConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            Profile userProfile = (Profile) request.getAttribute(ApplicationConstants.USER_PROFILE);
            String type = request.getHeader(ApplicationConstants.STOREFRONT_USER);
            if (type.equalsIgnoreCase(ApplicationConstants.CREATOR_PROFILE)) {
               CreatorProfile creatorProfile = creatorRepository.findById(userId).orElse(null);
               if (Objects.nonNull(creatorProfile)) {
                   validateBankAccountNumber(userBankDetails.getAccountNumber());
                   validateBankIFSCCode(userBankDetails.getBankIFSCCode());
                   validateUserPANDetails(userBankDetails.getPermanentAccountNumber());
               }
            }
            return new UserBankDetails();
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    private void validateUserPANDetails(String permanentAccountNumber) {
        try {

        } catch (Exception ex) {
            log.error("");
        }
    }

    private void validateBankIFSCCode(String bankIFSCCode) {
        try {

        } catch (Exception ex) {
            log.error("");
        }
    }

    private void validateBankAccountNumber(String accountNumber) {
        try {

        } catch (Exception ex) {
            log.error("");
        }

    }
}
