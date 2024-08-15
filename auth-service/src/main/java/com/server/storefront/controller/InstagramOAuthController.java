package com.server.storefront.controller;

import com.server.storefront.dto.IGUserProfileDTO;
import com.server.storefront.model.IGUserMedia;
import com.server.storefront.service.InstagramOAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
public class InstagramOAuthController {

    @Autowired
    private InstagramOAuthService instagramOAuthService;

    @PostMapping(path = "/oauth/access_token")
    public Mono<ResponseEntity<IGUserProfileDTO>> getUserAccessToken(HttpServletRequest request) {
        try {
            return instagramOAuthService.retrieveTokensAndUserDetails();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    @GetMapping(path = "/user_media")
    public Mono<ResponseEntity<List<IGUserMedia>>> getUserMediaObjects(@RequestBody String userName, HttpServletRequest request) {
        try {
            return instagramOAuthService.retrieveUserMedia(userName);
        } catch (Exception ex){
            log.error(ex.getMessage());
        }
        return null;
    }

}
