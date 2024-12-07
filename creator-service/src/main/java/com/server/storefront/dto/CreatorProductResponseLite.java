package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.server.storefront.helper.Views;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CreatorProductResponseLite {

    @JsonView({Views.Public.class, Views.Analytics.class})
    @JsonProperty("product_id")
    private String creatorProductId;

    @JsonView({Views.Public.class, Views.Analytics.class})
    @JsonProperty("title")
    private String productTitle;

    @JsonView({Views.Public.class, Views.Analytics.class})
    @JsonProperty("price")
    private long price;

    @JsonView({Views.Public.class, Views.Analytics.class})
    @JsonProperty("revenue")
    private long revenue;

    @JsonView({Views.Public.class, Views.Analytics.class})
    @JsonProperty("sale_unit")
    private long saleUnit;

    @JsonView(Views.Public.class)
    @JsonProperty("brand_id")
    private String partnerId;

    @JsonView(Views.Analytics.class)
    @JsonProperty("total_click")
    private int totalClick;

    @JsonView(Views.Analytics.class)
    @JsonProperty("unique_click")
    private int uniqueClick;

    @JsonView(Views.Analytics.class)
    @JsonProperty("category")
    private String category;

    public CreatorProductResponseLite(String creatorProductId, String productTitle, long price,
                                      long revenue,
                                      long saleUnit) {
        this.creatorProductId = creatorProductId;
        this.productTitle = productTitle;
        this.price = price;
        this.revenue = revenue;
        this.saleUnit = saleUnit;
    }
}
