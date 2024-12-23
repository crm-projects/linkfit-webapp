package com.server.storefront.controller;

import com.server.storefront.dto.IGMedia;
import com.server.storefront.dto.IGProfile;
import com.server.storefront.service.InstagramAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class InstagramAuthController {


    private final InstagramAuthService instagramAuthService;

    @PostMapping("/oauth/token")
    public Mono<ResponseEntity<IGProfile>> getUserAccessToken(HttpServletRequest request) {
        try {
            return instagramAuthService.retrieveTokensAndUserDetails();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    @GetMapping("/{user_name}/ig/media")
    public Mono<ResponseEntity<List<IGMedia>>> getUserMediaObjects(@PathVariable(name = "user_name") String userName, HttpServletRequest request) {
        try {
            return instagramAuthService.retrieveUserMedia(userName);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

}