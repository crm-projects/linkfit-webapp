package com.server.storefront.model.admin;

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
