package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseProfile {

    private String id = UUID.randomUUID().toString();

    @JsonProperty(value = "user_name")
    private String userName;

    @JsonProperty(value = "email_address")
    private String emailAddress;

    @JsonProperty(value = "encrypted_password")
    private String password;

    @JsonProperty(value = "display_name")
    private String displayName;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "gender")
    private String gender;

    @JsonProperty(value = "profile_pic_url")
    private String displayPicUrl;

    @JsonProperty(value = "active_ind")
    private boolean activeInd;

    @JsonProperty(value = "dob")
    private Date dateOfBirth;

    public BaseProfile(String email, String password) {
        this.emailAddress = email;
        this.password = password;
    }

}
