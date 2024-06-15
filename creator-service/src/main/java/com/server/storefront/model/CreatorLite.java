package com.server.storefront.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatorLite {

    @Nonnull
    @JsonProperty("userName")
    private String userName;

    @Nonnull
    @JsonProperty("emailAddress")
    private String emailAddress;

    @Nonnull
    @JsonProperty("gender")
    private String gender;

    @JsonProperty("isExistingCreator")
    private boolean isExistingCreator;

    @Nonnull
    @JsonProperty("selectedPlan")
    private PlanLite plan;

    @Nullable
    @JsonProperty("socialMediaProfiles")
    private List<PlatformLite> platformsList;
}
