package com.server.storefront.store.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.sql.Date;

@Data
@MappedSuperclass
public abstract class Store {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "STORE_URL")
    private String storeUrl;

    @Column(name = "VERIFIED_IND")
    private boolean isVerifiedInd;

    @Column(name = "ACTIVE_IND")
    private boolean isActiveInd;

    @Column(name = "CURRENCY_TYPE")
    private String currencyType;

    @Column(name = "CREATED_BY_TIME")
    private Date createdByTime;

    @Column(name = "CREATED_BY")
    private String createdBy;

}
