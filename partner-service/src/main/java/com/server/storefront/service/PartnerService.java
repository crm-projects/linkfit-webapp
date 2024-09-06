package com.server.storefront.service;

import com.server.storefront.dto.CampaignDTO;
import com.server.storefront.dto.PartnerDTO;
import com.server.storefront.exception.CampaignException;
import com.server.storefront.exception.PartnerException;

import java.util.List;
import java.util.Map;

public interface PartnerService {

    Map<String, Object> getAllPartnerDetails(int page, int limit) throws PartnerException;

    CampaignDTO launchCampaign(CampaignDTO campaignDTO) throws CampaignException;

    CampaignDTO getCampaignById(String campaignId) throws CampaignException;

    List<CampaignDTO> getAllCampaignByPartnerId(String partnerId) throws PartnerException, CampaignException;

    boolean deleteCampaignById(String campaignId) throws CampaignException;
}
