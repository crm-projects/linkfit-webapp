package com.server.storefront.controller;

import com.server.storefront.auth.model.UserBankDetails;
import com.server.storefront.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PaymentsVerificationController {

    @Autowired
    private PaymentService paymentService;


    @PostMapping(value = "/savePaymentDetails/{userId}")
    public ResponseEntity<UserBankDetails> getUserPaymentDetails(
            @RequestBody UserBankDetails bankDetails,
            @RequestParam String userId,
            HttpServletRequest request
    ) {
        try {
            return new ResponseEntity<>(paymentService.getUserPaymentDetails(bankDetails, userId, request), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
