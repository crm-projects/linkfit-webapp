package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.storefront.model.UserBankDetails;

import java.sql.Date;

public class CreatorDTO {

    private String id;
    @JsonProperty(value = "user_name")
    private String userName;

    @JsonProperty(value = "email_address")
    private String emailAddress;

    @JsonProperty(value = "display_name")
    private String displayName;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "gender")
    private String gender;

    @JsonProperty(value = "profile_pic_url")
    private String displayPicURL;

    @JsonProperty(value = "active_ind")
    private boolean activeInd;

    @JsonProperty(value = "is_insta_auth")
    private boolean instagramConnected;

    @JsonProperty(value = "d.o.b")
    private Date dateOfBirth;

    @JsonProperty(value = "bank_details")
    private UserBankDetails creatorBankDetails;

    @JsonProperty(value = "insta_profile_details")
    private IGUserProfileDTO instagramProfile;


}
