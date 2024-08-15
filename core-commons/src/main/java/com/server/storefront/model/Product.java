package com.server.storefront.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.sql.Date;

@Data
@MappedSuperclass
public abstract class Product {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "URL")
    private String productURL;

    @Column(name = "ACTIVE_IND")
    private boolean isActiveInd;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_TIME")
    private Date createdTime;


}
