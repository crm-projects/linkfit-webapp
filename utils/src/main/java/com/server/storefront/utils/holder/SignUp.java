package com.server.storefront.utils.holder;

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

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userEmail")
    private String userEmail;

    @JsonProperty("dob")
    private Date dateOfBirth;

    @JsonProperty("hasOTPInput")
    private boolean hasOTPInput;

    @JsonProperty("inputOTP")
    private String inputOTP;

    private boolean hasAuthToken;
}
