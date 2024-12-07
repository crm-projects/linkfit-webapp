package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.server.storefront.helper.Views;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class PartnerResponseLite {

    @JsonView({Views.Public.class, Views.Analytics.class})
    @JsonProperty("brand_id")
    private String brandId;

    @JsonView({Views.Public.class, Views.Analytics.class})
    @JsonProperty("brand_name")
    private String brandName;

    @JsonView({Views.Public.class, Views.Analytics.class})
    @JsonProperty("brand_name_formatted")
    private String brandNameFormatted;

    @JsonView({Views.Public.class, Views.Analytics.class})
    @JsonProperty("brand_url")
    private String brandUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartnerResponseLite that = (PartnerResponseLite) o;
        return Objects.equals(brandId, that.brandId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(brandId);
    }
}
