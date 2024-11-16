package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IGMedia {

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "media_type")
    private String mediaType;

    @JsonProperty(value = "thumbnail_url")
    private String thumbnailUrl;
}