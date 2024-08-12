package com.server.storefront.commons.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PARTNER_CAMPAIGN_DETAILS")
public class Campaign {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PLATFORM_COMMISSION")
    private int commissionRateForPlatform;

    @Column(name = "CREATOR_COMMISSION")
    private int commissionRateForCreator;

    @Column(name = "CREATOR_CRITERIA")
    private String creatorCriteria;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "ACTIVE_IND")
    private boolean activeInd;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "PRODUCT_ID")
    private Partner partner;

}
