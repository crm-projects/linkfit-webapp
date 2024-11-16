package com.server.storefront.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.storefront.constants.IGConstants;
import com.server.storefront.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


/**
 * To perform HTTP OAuth and Graph API requests to Instagram
 *
 * <p>Uses {@link WebClient#builder()} for asynchronous calls
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InstagramAuthServiceImpl implements InstagramAuthService {


    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<ResponseEntity<IGProfile>> retrieveTokensAndUserDetails() {

        WebClient webClient = webClientBuilder.baseUrl(IGConstants.BASE_URL).build();

        Mono<IGAuth> shortLivedToken = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(IGConstants.IG_OAUTH_URI)
                        .queryParam(IGConstants.CLIENT_ID, "")
                        .queryParam(IGConstants.CLIENT_SECRET, "")
                        .queryParam(IGConstants.GRANT_TYPE, "")
                        .queryParam(IGConstants.REDIRECT_URI, "")
                        .queryParam("code", "")
                        .build())
                .retrieve()
                .bodyToMono(IGAuth.class);
        return shortLivedToken.flatMap(exchangeToken -> {
            String token = exchangeToken.getAccessToken();

            Mono<IGProfile> userProfile = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(IGConstants.API_VERSION)
                            .path(IGConstants.ME)
                            .queryParam(IGConstants.IG_FIELDS, IGConstants.USER_PROFILE_FIELDS)
                            .queryParam(IGConstants.USER_ACCESS_TOKEN, token)
                            .build())
                    .retrieve()
                    .bodyToMono(IGProfile.class);

            return userProfile.map(user -> {
                        IGProfile userProfileDTO = new IGProfile();
                        userProfileDTO.setUserName(user.getUserName());
                        userProfileDTO.setFollowerCount(user.getFollowerCount());
                        userProfileDTO.setConnected(true);
                        return ResponseEntity.ok(userProfileDTO);
                    })
                    .onErrorResume(error -> Mono.just(ResponseEntity.status(500).body(null)));
        });
    }

    @Override
    public Mono<ResponseEntity<List<IGMedia>>> retrieveUserMedia(String userName) {
        WebClient webClient = webClientBuilder.baseUrl(IGConstants.BASE_URL).build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(IGConstants.API_VERSION)
                        .path("user_id")
                        .path(IGConstants.IG_MEDIA)
                        .queryParam(IGConstants.IG_FIELDS, IGConstants.USER_MEDIA_FIELDS)
                        .queryParam(IGConstants.USER_ACCESS_TOKEN, "token")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        Map<String, Object> responseMap = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {
                        });
                        List<Map<String, Object>> responseList = (List<Map<String, Object>>) responseMap.get("data");
                        List<IGMedia> mediaList = objectMapper.convertValue(responseList, new TypeReference<List<IGMedia>>() {
                        });
                        return Mono.just(ResponseEntity.ok(mediaList));

                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException("Failed to parse API response", e));
                    }
                });
    }
}
