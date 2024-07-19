package com.server.storefront.model.auth;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistration {

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("emailAddress")
    private String userEmail;

    @JsonProperty("password")
    private String userPassword;
}
