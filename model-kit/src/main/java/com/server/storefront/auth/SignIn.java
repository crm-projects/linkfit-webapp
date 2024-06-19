package com.server.storefront.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignIn {

    @JsonProperty("key")
    private String userEmail;

    @JsonProperty("password")
    private String userPassword;

}
