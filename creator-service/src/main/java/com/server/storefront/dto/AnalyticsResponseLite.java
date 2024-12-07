package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.server.storefront.helper.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class AnalyticsResponseLite {

    @JsonView(Views.Public.class)
    @JsonProperty("brands_list")
    private List<PartnerResponseLite> brandList;

    @JsonView(Views.Public.class)
    @JsonProperty("products")
    private List<CreatorProductResponseLite> productList;

    @JsonView(Views.Public.class)
    @JsonProperty("pagination")
    private Map<String, Object> pagination;

    @JsonView(Views.Analytics.class)
    @JsonProperty("product")
    private CreatorProductResponseLite productInsight;

    @JsonView(Views.Analytics.class)
    @JsonProperty("brand")
    private PartnerResponseLite partner;

    public AnalyticsResponseLite(CreatorProductResponseLite productInsight, PartnerResponseLite partner) {
        this.productInsight = productInsight;
        this.partner = partner;
    }

    public AnalyticsResponseLite(List<PartnerResponseLite> partners, List<CreatorProductResponseLite> products, Map<String, Object> pagination) {
        this.pagination = pagination;
        this.brandList = partners;
        this.productList = products;
    }
}
