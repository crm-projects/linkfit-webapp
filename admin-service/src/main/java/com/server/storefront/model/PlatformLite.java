package com.server.storefront.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlatformLite {

    @JsonProperty("platformId")
    private String platformId;

    @JsonProperty("linkedInd")
    private boolean linkedInd;

    @JsonProperty("activeInd")
    private boolean activeInd;
}
