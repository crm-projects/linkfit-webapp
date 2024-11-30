package com.server.storefront.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CREATOR_PRODUCT")
public class CreatorProduct {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AFFILIATE_CODE")
    private String affiliateCode;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "PRICE")
    private long price;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "ACTIVE_IND")
    private boolean activeInd;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_TIME")
    private Date createdTime;

    @Column(name = "AFFILIATE_EXPIRES_AT")
    private Date affiliateExpiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATOR_ID", nullable = false)
    private CreatorProfile creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

}
