package com.server.storefront.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "COLLECTION")
public class Collection {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IMAGE_URL")
    private String imageURL;

    @Column(name = "ACTIVE_IND")
    private boolean activeInd;

    @Nullable
    @OneToOne
    @JoinColumn(name = "MEDIA_ID")
    private MediaData mediaData;

    @ManyToMany
    @JoinTable(name = "COLLECTION_PRODUCT_MAPPING",
            joinColumns = @JoinColumn(name = "COLLECTION_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "CREATOR_PRODUCT_ID", referencedColumnName = "ID"))
    private Set<CreatorProduct> creatorProducts;

}
