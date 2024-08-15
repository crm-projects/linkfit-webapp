package com.server.storefront.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IGUserMedia {

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "media_type")
    private String mediaType;

    @JsonProperty(value = "thumbnail_url")
    private String thumbnailURL;

}
