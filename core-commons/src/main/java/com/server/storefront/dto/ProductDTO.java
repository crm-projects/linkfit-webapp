package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("product_title")
    private String productTitle;

    @JsonProperty("product_url")
    private String productURL;

    @JsonProperty("partner_name")
    private String partnerName;

    @JsonProperty("price")
    private long price;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("category")
    private String category;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("affiliate_url")
    private String affiliateCode;
}
