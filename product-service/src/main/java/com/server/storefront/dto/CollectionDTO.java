package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.storefront.model.MediaData;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionDTO {

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

    @JsonProperty("media_data")
    private MediaData mediaData;

    @JsonProperty("product_count")
    private int count;

    @JsonProperty("product_list")
    private List<CreatorProductDTO> product;
}
