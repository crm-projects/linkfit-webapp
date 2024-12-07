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
@Table(name = "CLICK")
public class Click {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "CREATOR_PRODUCT_ID", nullable = false)
    private String creatorProductId;

    @Column(name = "TOTAL_CLICKS")
    private int totalClicks;

    @Column(name = "UNIQUE_CLICKS")
    private int uniqueClicks;
}
