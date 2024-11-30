package com.server.storefront.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.server.storefront.helper.Views;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "MEDIA_DATA")
public class CollectionMedia {

    @Id
    @JsonView(Views.Public.class)
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @JsonView(Views.Public.class)
    @JsonProperty("media_id")
    @Column(name = "MEDIA_ID")
    private String mediaId;

    @JsonView(Views.Public.class)
    @JsonProperty("media_type")
    @Column(name = "MEDIA_TYPE")
    private String mediaType;

    @JsonView(Views.Public.class)
    @JsonProperty("media_source")
    @Column(name = "MEDIA_SOURCE")
    private String mediaSource;

    @JsonView(Views.Public.class)
    @JsonProperty("thumbnail_url")
    @Column(name = "THUMBNAIL_URL")
    private String thumbNailURL;

    @JsonView(Views.Public.class)
    @Column(name = "ACTIVE_IND")
    private boolean activeInd;

}
