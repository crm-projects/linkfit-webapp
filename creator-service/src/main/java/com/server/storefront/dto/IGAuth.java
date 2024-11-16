package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IGAuth {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "permissions")
    private List<String> permissions;

    @Nullable
    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private long expiresIn;
}
