package com.server.storefront.controller;

import com.server.storefront.dto.IGUserProfileDTO;
import com.server.storefront.model.IGUserMedia;
import com.server.storefront.service.InstagramOAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class InstagramOAuthController {

    private final InstagramOAuthService instagramOAuthService;

    @PostMapping("/oauth/access_token")
    public Mono<ResponseEntity<IGUserProfileDTO>> getUserAccessToken(HttpServletRequest request) {
        try {
            return instagramOAuthService.retrieveTokensAndUserDetails();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    @GetMapping("/user_media")
    public Mono<ResponseEntity<List<IGUserMedia>>> getUserMediaObjects(@RequestBody String userName, HttpServletRequest request) {
        try {
            return instagramOAuthService.retrieveUserMedia(userName);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

}
