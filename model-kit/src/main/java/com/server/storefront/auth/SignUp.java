package com.server.storefront.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUp {

    @JsonProperty("id")
    private String creatorId;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userEmail")
    private String userEmail;

    @JsonProperty("password")
    private String password;

    @JsonProperty("dob")
    private Date dateOfBirth;

    @JsonProperty("hasOTPInput")
    private boolean hasOTPInput;

    @JsonProperty("inputOTP")
    private String inputOTP;

    @JsonIgnore
    private boolean hasAuthToken;
}
