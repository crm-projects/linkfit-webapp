package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.storefront.model.MediaData;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CollectionLite {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image_url")
    private String imageURL;

    @Transient
    @JsonProperty("media_ind")
    private boolean mediaInd;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("media_data")
    private MediaData mediaData;

    @JsonProperty("creatorId")
    private String creatorId;

    @Transient
    @JsonProperty("product_count")
    private int count;

    @JsonProperty("product_list")
    private Set<CreatorProductDTO> products;
}
