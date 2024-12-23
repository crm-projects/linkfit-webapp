package com.server.storefront.controller;

import com.server.storefront.model.UserBankDetails;
import com.server.storefront.service.PaymentService;
import com.server.storefront.service.AmazonS3Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserProfileController {

    private final PaymentService paymentService;

    private final AmazonS3Service amazonS3Service;

    @Value("${aws.s3.bucket}")
    private static String bucket;

    @PostMapping("/{user_name}/payment/info/save")
    public ResponseEntity<UserBankDetails> getUserPaymentDetails(
            @RequestBody UserBankDetails bankDetails,
            @PathVariable(name = "user_name") String userId,
            HttpServletRequest request) {
        try {
            return new ResponseEntity<>(paymentService.getUserPaymentDetails(bankDetails, userId, request), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @PostMapping("/{user_name}/upload")
    public ResponseEntity<String> uploadImage(@PathVariable("user_name") String userName,
                                              @RequestParam("file") @Valid MultipartFile file) {
        try {
            String url = amazonS3Service.upload(file,userName);
            return ResponseEntity.ok(url);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("File upload failed: " + ex.getMessage());
        }
    }

    @GetMapping("/{user_name}/meta")
    public ResponseEntity<String> getCreatorProfileInfo(@PathVariable("user_name") String userName, HttpServletRequest request) {
        try {
            return ResponseEntity.ok("");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("");
        }
    }


}
