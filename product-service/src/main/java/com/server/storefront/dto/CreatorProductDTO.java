package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
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
    private String affiliateUrl;

    @JsonProperty("created_at")
    private Date createdTime;

    @JsonProperty("expires_at")
    private Date expiryDate;
}
