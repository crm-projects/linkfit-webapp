package com.server.storefront.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @JsonProperty("userEmail")
    private String userEmail;

    @JsonProperty("password")
    private String userPassword;

}
