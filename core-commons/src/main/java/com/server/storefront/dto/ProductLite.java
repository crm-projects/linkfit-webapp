package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.server.storefront.helper.Views;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductLite {

    @JsonView(Views.Public.class)
    @JsonProperty("product_id")
    private String productId;

    @JsonView(Views.Public.class)
    @JsonProperty("product_title")
    private String productTitle;

    @JsonView(Views.Public.class)
    @JsonProperty("product_url")
    private String productURL;

    @JsonView(Views.Public.class)
    @JsonProperty("partner_name")
    private String partnerName;

    @JsonView(Views.Public.class)
    @JsonProperty("price")
    private long price;

    @JsonView(Views.Public.class)
    @JsonProperty("currency")
    private String currency;

    @JsonView(Views.Public.class)
    @JsonProperty("category")
    private String category;

    @JsonView(Views.Public.class)
    @JsonProperty("description")
    private String description;

    @JsonView(Views.Public.class)
    @JsonProperty("image_url")
    private String imageUrl;

    @JsonView(Views.Public.class)
    @JsonProperty("affiliate_url")
    private String affiliateCode;
}
