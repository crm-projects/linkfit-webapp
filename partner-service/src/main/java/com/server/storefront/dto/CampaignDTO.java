package com.server.storefront.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDTO {

    private String id;

    private String partnerId;

    private String title;

    private String description;

    private Date startDate;

    private Date endDate;

    private int commissionRateForCreator;

    private CreatorCriteriaDTO creatorCriteria;

    private boolean activeInd;

}
