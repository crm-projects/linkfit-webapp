package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.server.storefront.helper.Views;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CreatorProductLite {

    @JsonView(Views.Public.class)
    @JsonProperty("id")
    private String id;

    @JsonView(Views.Public.class)
    @JsonProperty("p_id")
    private String pid;

    @JsonView(Views.Public.class)
    @JsonProperty("title")
    private String title;

    @JsonView(Views.Public.class)
    @JsonProperty("price")
    private long price;

    @JsonView(Views.Public.class)
    @JsonProperty("image_url")
    private String imageURL;

    @JsonView(Views.Public.class)
    @JsonProperty("affiliate_url")
    private String affiliateUrl;

    @JsonView(Views.Public.class)
    @JsonProperty("currency")
    private String currency;

    @JsonView(Views.Public.class)
    @JsonProperty("category")
    private String category;

    @JsonView(Views.Public.class)
    @Transient
    @JsonProperty("created_at")
    private Date createdTime;

    @JsonView(Views.Public.class)
    @Transient
    @JsonProperty("expires_at")
    private Date expiryDate;
}
