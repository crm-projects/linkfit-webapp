package com.server.storefront.service;

import com.server.storefront.commons.model.Campaign;
import com.server.storefront.commons.model.Partner;
import com.server.storefront.dto.CampaignDTO;
import com.server.storefront.dto.PartnerDTO;

import java.util.List;
import java.util.Optional;

public interface PartnerService {

    List<PartnerDTO> getAllPartnerDetails();

    CampaignDTO launchCampaign(CampaignDTO campaignDTO);

    CampaignDTO getCampaignById(String campaignId);

    List<CampaignDTO> getAllCampaignByPartnerId(String partnerId);

    boolean deleteCampaignById(String campaignId);
}
