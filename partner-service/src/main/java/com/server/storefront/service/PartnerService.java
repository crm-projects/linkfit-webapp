package com.server.storefront.service;

import com.server.storefront.dto.CampaignDTO;
import com.server.storefront.dto.PartnerDTO;

import java.util.List;

public interface PartnerService {

    List<PartnerDTO> getAllPartnerDetails();

    CampaignDTO launchCampaign(CampaignDTO campaignDTO);

    CampaignDTO getCampaignById(String campaignId);

    List<CampaignDTO> getAllCampaignByPartnerId(String partnerId);

    boolean deleteCampaignById(String campaignId);
}