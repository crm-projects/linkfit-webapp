package com.server.storefront.service;

import com.server.storefront.dto.CampaignLite;
import com.server.storefront.exception.CampaignException;
import com.server.storefront.exception.PartnerException;

import java.util.List;
import java.util.Map;

public interface PartnerService {

    Map<String, Object> getAllPartnerDetails(int page, int limit) throws PartnerException;

    CampaignLite launchCampaign(CampaignLite campaignLite) throws CampaignException;

    CampaignLite getCampaignById(String campaignId) throws CampaignException;

    List<CampaignLite> getAllCampaignByPartnerId(String partnerId) throws PartnerException, CampaignException;

    boolean deleteCampaignById(String campaignId) throws CampaignException;
}
