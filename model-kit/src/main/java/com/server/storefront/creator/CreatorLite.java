package com.server.storefront.creator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.storefront.admin.PlanLite;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatorLite {

    @Nullable
    @JsonProperty("creatorId")
    private String creatorId;

    @Nonnull
    @JsonProperty("userName")
    private String userName;

    @Nonnull
    @JsonProperty("emailAddress")
    private String emailAddress;

    @Nonnull
    @JsonProperty("gender")
    private String gender;

    @Nullable
    @JsonProperty("dob")
    private Date dateOfBirth;

    @Nullable
    @JsonProperty("description")
    private String description;

    @Nullable
    @JsonProperty("displayPicURL")
    private String displayPicURL;

    @JsonProperty("selectedPlan")
    private PlanLite plan;

}
