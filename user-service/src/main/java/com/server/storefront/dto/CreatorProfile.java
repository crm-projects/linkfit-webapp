package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

@Getter
@Setter
@NoArgsConstructor
public class CreatorProfile extends BaseProfile {


    public CreatorProfile(String email, String password) {
        super(email, password);
    }

    @Transient
    private boolean isInstaConnected;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonProperty(value = "insta_profile")
//    private IGProfile instagramProfile;

//    @JsonProperty(value = "bank_details")
//    private UserBankDetails creatorBankDetails;

}
