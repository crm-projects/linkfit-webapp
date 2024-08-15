package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerDTO {


    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "domain")
    private String domain;

    @JsonProperty(value = "affiliateValue")
    private int affiliateValue;
}
