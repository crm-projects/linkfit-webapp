package com.server.storefront.controller;

import com.server.storefront.model.UserBankDetails;
import com.server.storefront.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1")
@RestController
public class PaymentsAuthController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/savePaymentDetails/{userId}")
    public ResponseEntity<UserBankDetails> getUserPaymentDetails(
            @RequestBody UserBankDetails bankDetails,
            @RequestParam(name = "userId") String userId,
            HttpServletRequest request) {
        try {
            return new ResponseEntity<>(paymentService.getUserPaymentDetails(bankDetails, userId, request), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
