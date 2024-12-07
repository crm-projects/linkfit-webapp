package com.server.storefront.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PARTNER")
public class Partner {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String domain;

    @Column(name = "AFFILIATE_PERCENT")
    private int affiliatePercentage;

    @Column(name = "CREATED_TIME")
    private Date createdTime;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "MODIFIED_TIME")
    private Date modifiedTime;

    @Column(name = "ACTIVE_IND")
    private boolean activeInd;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Nullable
    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL)
    private List<Campaign> campaignList;

}
