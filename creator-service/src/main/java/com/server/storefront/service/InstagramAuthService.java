package com.server.storefront.service;

import com.server.storefront.dto.IGMedia;
import com.server.storefront.dto.IGProfile;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface InstagramAuthService {

    Mono<ResponseEntity<IGProfile>> retrieveTokensAndUserDetails();

    Mono<ResponseEntity<List<IGMedia>>> retrieveUserMedia(String userName);
}
