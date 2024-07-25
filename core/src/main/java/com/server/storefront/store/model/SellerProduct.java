package com.server.storefront.store.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@Data
@Entity
@Table(name = "SELLER_PRODUCTS")
@EqualsAndHashCode(callSuper = true)
public class SellerProduct extends Product {

    @Column(name = "CATEGORY_ID")
    private String categoryId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SELLING_PRICE")
    private double sellingPrice;

    @Column(name = "PURCHASE_PRICE")
    private double purchasePrice;

    @Column(name = "PRODUCT_VARIANTS")
    private int productVariants;

    @Column(name = "DISCOUNT_IND")
    private boolean discountInd;

    @Column(name = "DISCOUNT_VALUE")
    private double discountValue;

    @Column(name = "REORDER_POINTS")
    private int reOrderPoints;

    @Column(name = "VISIBLE_IND")
    private boolean isVisibleInd;

    @Column(name = "PUBLISHED_TIME")
    private Date publishedTime;

    @Column(name = "LAST_MODIFIED_TIME")
    private Date lastModifiedTime;

    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @Column(name = "UNITS")
    private String units;

    @Column(name = "IDENTIFIER_KEY")
    private String identifierKey;

    @Column(name = "IDENTIFIER_VALUE")
    private String identifierValue;

    @Column(name = "OPENING_STOCK")
    private int openingStock;

    @Column(name = "TAX_PERCENTAGE")
    private double taxPercentage;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private SellerStore store;
}
