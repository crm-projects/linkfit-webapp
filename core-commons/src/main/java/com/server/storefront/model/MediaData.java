package com.server.storefront.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "MEDIA_DATA")
public class MediaData {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "MEDIA_ID")
    private String mediaId;

    @Column(name = "MEDIA_TYPE")
    private String mediaType;

    @Column(name = "MEDIA_SOURCE")
    private String mediaSource;

    @Column(name = "THUMBNAIL_URL")
    private String thumbNailURL;

    @Column(name = "ACTIVE_IND")
    private boolean activeInd;

}
