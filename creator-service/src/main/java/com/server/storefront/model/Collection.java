package com.server.storefront.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.server.storefront.helper.Views;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "COLLECTION")
public class Collection {

    @Id
    @JsonView(Views.Public.class)
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @JsonView(Views.Public.class)
    @Column(name = "NAME")
    private String name;

    @JsonView(Views.Public.class)
    @Column(name = "DESCRIPTION")
    private String description;

    @JsonView(Views.Public.class)
    @Column(name = "IMAGE_URL")
    private String imageURL;

    @JsonView(Views.Public.class)
    @Column(name = "ACTIVE_IND")
    private boolean activeInd;

    @JsonView(Views.Private.class)
    @Column(name = "CREATOR_ID")
    private String creatorId;

    @OneToOne
    @JsonView(Views.Public.class)
    @JoinColumn(name = "MEDIA_ID")
    private CollectionMedia collectionMedia;

    @JsonView(Views.Private.class)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "COLLECTION_PRODUCT_MAPPING",
            joinColumns = @JoinColumn(name = "COLLECTION_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "CREATOR_PRODUCT_ID", referencedColumnName = "ID"))
    private Set<CreatorProduct> creatorProducts;

}
