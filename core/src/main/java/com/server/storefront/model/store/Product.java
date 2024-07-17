package com.server.storefront.model.store;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Entity
@Table(name = "PRODUCT")
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "ID")
    private String id;

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

    @Column(name = "IS_DISCOUNT_IND")
    private boolean discountInd;

    @Column(name = "DISCOUNT_VALUE")
    private double discountValue;

    @Column(name = "REORDER_POINTS")
    private int reOrderPoints;

    @Column(name = "IS_ACTIVE_IND")
    private boolean isActiveInd;

    @Column(name = "IS_VISIBLE_IND")
    private boolean isVisibleInd;

    @Column(name = "PUBLISHED_TIME")
    private Date publishedTime;

    @Column(name = "LAST_MODIFIED_TIME")
    private Date lastModifiedTime;

    @Column(name = "CREATED_TIME")
    private Date createdTime;

    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "URL")
    private String productURL;

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
    private Store store;



}
