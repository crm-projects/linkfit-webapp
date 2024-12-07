package com.server.storefront.model;

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
@Table(name = "PRODUCT_ANALYTICS")
public class ProductAnalytics implements Comparable<ProductAnalytics>{

    @Id
    @Column(name = "ID")
    private  String id = UUID.randomUUID().toString();

    @Column(name = "CREATOR_PRODUCT_ID")
    private  String productId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "REVENUE")
    private long revenue;

    @Column(name = "UNIT_SOLD")
    private int unitSold;

    @Column(name = "UNIT_RETURN")
    private int unitReturn;

    @Column(name = "PRICE")
    private long price;

    @Override
    public String toString() {
        return "ProductAnalytics{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", title='" + title + '\'' +
                ", revenue=" + revenue +
                ", unitSold=" + unitSold +
                ", unitReturn=" + unitReturn +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", partnerId='" + partnerId + '\'' +
                '}';
    }

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "PARTNER_ID")
    private String partnerId;

    @Override
    public int compareTo(ProductAnalytics o) {
        return this.unitSold - o.unitSold;
    }
}