package com.server.storefront.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CampaignLite {

    private String id;

    private String partnerId;

    private String title;

    private String description;

    private Date startDate;

    private Date endDate;

    private int commissionRateForCreator;

    private CriteriaLite creatorCriteria;

    private boolean activeInd;

}
