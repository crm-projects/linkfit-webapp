package com.server.storefront.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatorSMMapLite {

    @Nonnull
    @JsonProperty("creatorId")
    private String creatorId;

    @Nonnull
    @JsonProperty("platformId")
    private String platformId;

    @Nonnull
    @JsonProperty("platformId")
    private String socialMediaId;

    @Nonnull
    @JsonProperty("linkedInd")
    private boolean linkedInd;

    @Nonnull
    @JsonProperty("activeInd")
    private boolean activeInd;
}
