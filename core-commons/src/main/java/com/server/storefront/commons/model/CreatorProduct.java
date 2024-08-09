package com.server.storefront.commons.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "CREATOR_PRODUCTS")
@EqualsAndHashCode(callSuper = true)
public class CreatorProduct extends Product {

    @Column(name = "AFFILIATE_URL")
    private String affiliateURL;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private CreatorStore store;

}
