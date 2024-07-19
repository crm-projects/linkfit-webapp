package com.server.storefront.controller;

import com.server.storefront.model.auth.User;
import com.server.storefront.model.auth.UserRegistration;
import com.server.storefront.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @PostMapping("/api/register")
    public ResponseEntity<UserRegistration> userRegistration(@RequestBody UserRegistration user, HttpServletRequest request) {
        log.info("Preparing Registration for user: {}", user.getUserName());
        try {
            return new ResponseEntity<>(securityService.validateAndRegisterUser(user), HttpStatus.OK);
        } catch ( Exception ex) {
            log.error("Error: {} while authenticating User: {}", ex.getMessage(), user.getUserName());
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PostMapping("/api/login")
    public ResponseEntity<User> userLogin(@RequestBody User user, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(securityService.validateAndLoginUser(user), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error: {} while User Login ", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }


}
