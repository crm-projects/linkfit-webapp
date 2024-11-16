package com.server.storefront.controller;


import com.server.storefront.dto.Credentials;
import com.server.storefront.service.StartupService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class StartupController {

    private final StartupService startupService;

    private static final String JWT = "jwt";

    private static final String USER_ID = "userId";


    /**
     * Generate otp response entity.
     *
     * @param credentials the credentials
     * @param request     the request
     * @return the response entity
     */
    @PostMapping("/otp/generate")
    public ResponseEntity<Boolean> generateOtp(@RequestBody @NonNull Credentials credentials, HttpServletRequest request) {
        log.info("Processing OTP for {}", credentials.getKey());
        try {
            return ResponseEntity.ok(startupService.generateOtpByEmail(credentials));
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return ResponseEntity.ok(false);
    }


    /**
     * Validate username response entity.
     *
     * @param userName the user name
     * @param request  the request
     * @return the response entity
     */
    @GetMapping("/check/username")
    public ResponseEntity<Boolean> validateUsername(@RequestParam("id") String userName, HttpServletRequest request) {
        return ResponseEntity.ok(startupService.validateUsername(userName));
    }

    /**
     * Validate and register user response entity.
     *
     * @param userSignUpDetails the user sign up details
     * @param request           the request
     * @return the response entity
     */
    @PostMapping("/otp/validate")
    public ResponseEntity<String> validateAndRegisterUser(@RequestBody Map<String, String> userSignUpDetails,
                                                          @RequestParam(name = "profile") String profile,
                                                          HttpServletRequest request
    ) {
        log.info("OTP Validation for: {} has started at: {}", userSignUpDetails.get("email"), new Date());
        try {
            Map<String, Object> userData = startupService.processUserRegistration(userSignUpDetails, profile);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, userData.get(JWT).toString())
                    .body(userData.get(USER_ID).toString());
        } catch (Exception ex) {
            //TODO : Throw a dedicated exception.
            log.error(ex.getMessage());
        }
        return null;
    }


    /**
     * User login response entity.
     *
     * @param credentials the credentials
     * @param request     the request
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody Credentials credentials, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(startupService.authorizeUserLogin(credentials));
        } catch (Exception ex) {
            //TODO : Throw dedicated exception.
            log.error(ex.getMessage());
        }
        return ResponseEntity.ok("");
    }

}