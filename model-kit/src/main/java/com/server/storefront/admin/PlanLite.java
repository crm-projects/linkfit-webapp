package com.server.storefront.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlanLite {

    @JsonProperty("planId")
    private String planId;

    @JsonProperty("activeInd")
    private boolean activeInd;

    @JsonProperty("isFreeTrial")
    private boolean isFreeTrial;
}
