package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatorProductDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("p_id")
    private String pid;

    @JsonProperty("title")
    private String title;

    @JsonProperty("price")
    private long price;

    @JsonProperty("image_url")
    private String imageURL;

    @JsonProperty("affiliate_url")
    private String affiliateCode;
}
