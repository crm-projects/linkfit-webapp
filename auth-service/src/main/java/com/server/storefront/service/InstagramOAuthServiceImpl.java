package com.server.storefront.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.storefront.constants.GraphAPI;
import com.server.storefront.dto.IGAccessTokenDTO;
import com.server.storefront.dto.IGUserProfileDTO;
import com.server.storefront.model.IGUserMedia;
import com.server.storefront.model.IGUserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class InstagramOAuthServiceImpl implements InstagramOAuthService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<ResponseEntity<IGUserProfileDTO>> retrieveTokensAndUserDetails() {
        // TODO: Find an efficient and secure method for retrieving and storing USER_ACCESS_TOKEN.
        //  Consider using encryption for storage, and focus on security best practices (e.g., secure storage solutions).
        WebClient webClient = webClientBuilder.baseUrl(GraphAPI.BASE_URL).build();

        Mono<IGAccessTokenDTO> shortLivedToken = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GraphAPI.IG_OAUTH_URI)
                        .queryParam(GraphAPI.CLIENT_ID, "")
                        .queryParam(GraphAPI.CLIENT_SECRET, "")
                        .queryParam(GraphAPI.GRANT_TYPE, "")
                        .queryParam(GraphAPI.REDIRECT_URI, "")
                        .queryParam("code", "")
                        .build())
                .retrieve()
                .bodyToMono(IGAccessTokenDTO.class);
        return shortLivedToken.flatMap(exchangeToken -> {
            String token = exchangeToken.getAccessToken();

            Mono<IGUserProfile> userProfile = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(GraphAPI.API_VERSION)
                            .path(GraphAPI.ME)
                            .queryParam(GraphAPI.IG_FIELDS, GraphAPI.USER_PROFILE_FIELDS)
                            .queryParam(GraphAPI.USER_ACCESS_TOKEN, token)
                            .build())
                    .retrieve()
                    .bodyToMono(IGUserProfile.class);

            return userProfile.map(user -> {
                        IGUserProfileDTO userProfileDTO = new IGUserProfileDTO();
                        userProfileDTO.setUserName(user.getUserName());
                        userProfileDTO.setFollowersCount(user.getFollowersCount());
                        userProfileDTO.setConnected(true);
                        return ResponseEntity.ok(userProfileDTO);
                    })
                    .onErrorResume(error -> Mono.just(ResponseEntity.status(500).body(null)));
        });
    }


    @Override
    public Mono<ResponseEntity<List<IGUserMedia>>> retrieveUserMedia(String userName) {
        // TODO: Find an efficient and secure method for retrieving and storing USER_ACCESS_TOKEN.
        //  Consider using encryption for storage, and focus on security best practices (e.g., secure storage solutions).

        WebClient webClient = webClientBuilder.baseUrl(GraphAPI.BASE_URL).build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GraphAPI.API_VERSION)
                        .path("user_id")
                        .path(GraphAPI.IG_MEDIA)
                        .queryParam(GraphAPI.IG_FIELDS, GraphAPI.USER_MEDIA_FIELDS)
                        .queryParam(GraphAPI.USER_ACCESS_TOKEN, "token")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        Map<String, Object> responseMap = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {
                        });
                        List<Map<String, Object>> responseList = (List<Map<String, Object>>) responseMap.get("data");
                        List<IGUserMedia> mediaList = objectMapper.convertValue(responseList, new TypeReference<List<IGUserMedia>>() {
                        });
                        return Mono.just(ResponseEntity.ok(mediaList));

                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException("Failed to parse API response", e));
                    }
                });
    }
}
