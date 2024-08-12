package com.server.storefront.service;

import com.server.storefront.commons.Util;
import com.server.storefront.commons.model.Campaign;
import com.server.storefront.commons.model.Partner;
import com.server.storefront.commons.repository.PartnerRepository;
import com.server.storefront.dto.CampaignDTO;
import com.server.storefront.dto.CreatorCriteriaDTO;
import com.server.storefront.dto.PartnerDTO;
import com.server.storefront.repository.CreatorCampaignRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PartnerServiceImpl implements PartnerService {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private CreatorCampaignRepository campaignRepository;

    @Override
    @Cacheable
    @Transactional(readOnly = true)
    public List<PartnerDTO> getAllPartnerDetails() {
        List<PartnerDTO> partnerList = new ArrayList<>();
        log.info("Fetching Partners List from the Database");
        try {
            List<Partner> partners = partnerRepository.findAllByActiveInd(true);
            scrubPartnerItems(partners, partnerList);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return partnerList;
    }

    @Override
    @Transactional
    public CampaignDTO launchCampaign(CampaignDTO campaignDTO) {
        if (Objects.isNull(campaignDTO)) {
            return null;
        }
        try {
            if (StringUtils.hasText(campaignDTO.getPartnerId())) {
                Partner partner = partnerRepository.findById(campaignDTO.getPartnerId()).orElse(null);
                if (Objects.nonNull(partner)) {
                    processCreatorCampaignItems(campaignDTO, partner);
                    return campaignDTO;
                } else {
                    log.error("");
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    private void processCreatorCampaignItems(CampaignDTO campaignDTO, Partner partner) {
        Date now = new Date();
        Campaign campaign = new Campaign();
        if (StringUtils.hasLength(campaignDTO.getId())) {
            campaign = campaignRepository.findById(campaignDTO.getId()).orElse(null);
            if (Objects.nonNull(campaign)) {
                createUpdateCampaignItems(campaign, campaignDTO, partner);
            }
        } else {
            campaign.setStartDate(now);
            createUpdateCampaignItems(campaign, campaignDTO, partner);
        }
    }

    private void createUpdateCampaignItems(Campaign campaign, CampaignDTO campaignDTO, Partner partner) {
        campaign.setTitle(campaignDTO.getTitle());
        campaign.setDescription(campaign.getDescription());
        campaign.setCommissionRateForPlatform(0);
        campaign.setCommissionRateForCreator(campaignDTO.getCommissionRateForCreator());
        campaign.setEndDate(campaignDTO.getEndDate());
        campaign.setCreatorCriteria(Util.convertJSONToString(campaignDTO.getCreatorCriteria()));
        campaign.setActiveInd(true);
        campaign.setPartner(partner);

        campaignRepository.save(campaign);
        updatePartnerItems(campaign, partner);
    }

    private void updatePartnerItems(Campaign campaign, Partner partner) {
        List<Campaign> campaignList = new ArrayList<>();
        campaignList.add(campaign);
        if (partner.getCampaignList().isEmpty()) {
            partner.setCampaignList(campaignList);
        } else {
            campaignList.addAll(partner.getCampaignList());
            partner.setCampaignList(campaignList);
        }
        partnerRepository.save(partner);
    }

    @Override
    @Transactional(readOnly = true)
    public CampaignDTO getCampaignById(String campaignId) {
        if (!StringUtils.hasLength(campaignId)) {
            throw new RuntimeException("");
        }
        Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
        if (Objects.nonNull(campaign)) {
            CampaignDTO campaignDTO = new CampaignDTO();
            campaignDTO.setTitle(campaign.getTitle());
            campaignDTO.setDescription(campaign.getDescription());
            campaignDTO.setCommissionRateForCreator(campaign.getCommissionRateForCreator());
            campaignDTO.setStartDate(campaign.getStartDate());
            campaignDTO.setEndDate(campaign.getEndDate());
            campaignDTO.setActiveInd(campaign.isActiveInd());
            CreatorCriteriaDTO creatorCriteriaDTO = Util.convertStringToJSON(campaign.getCreatorCriteria(), CreatorCriteriaDTO.class);
            campaignDTO.setCreatorCriteria(creatorCriteriaDTO);
            return campaignDTO;
        } else {
            log.error("");
            throw new RuntimeException("");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampaignDTO> getAllCampaignByPartnerId(String partnerId) {
        if (!StringUtils.hasLength(partnerId)) {
            throw new RuntimeException("");
        }
        List<CampaignDTO> campaignList = new ArrayList<>();
        try {
            Partner partner = partnerRepository.findById(partnerId).orElse(null);
            if (Objects.nonNull(partner)) {
                List<Campaign> campaigns = partner.getCampaignList();
                if (!CollectionUtils.isEmpty(campaigns)) {
                    for (Campaign campaign : campaigns) {
                        CampaignDTO campaignDTO = new CampaignDTO();
                        campaignDTO.setTitle(campaign.getTitle());
                        campaignDTO.setDescription(campaign.getDescription());
                        campaignDTO.setStartDate(campaign.getStartDate());
                        campaignDTO.setEndDate(campaign.getEndDate());
                        campaignDTO.setCommissionRateForCreator(campaign.getCommissionRateForCreator());
                        campaignDTO.setActiveInd(campaign.isActiveInd());
                        campaignDTO.setCreatorCriteria(Util.convertStringToJSON(campaign.getCreatorCriteria(), CreatorCriteriaDTO.class));
                        campaignList.add(campaignDTO);
                    }
                }
                return campaignList;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return campaignList;
    }

    @Override
    @Transactional
    public boolean deleteCampaignById(String campaignId) {
        if (!StringUtils.hasLength(campaignId)) {
            throw new RuntimeException("");
        }
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElse(null);
        if (Objects.nonNull(existingCampaign)) {
            existingCampaign.setActiveInd(false);
            campaignRepository.save(existingCampaign);
            return true;
        }
        return false;
    }

    private void scrubPartnerItems(List<Partner> partners, List<PartnerDTO> partnerList) {
        if (!CollectionUtils.isEmpty(partners)) {
            for (Partner partner : partners) {
                PartnerDTO partnerDTO = new PartnerDTO();
                partnerDTO.setName(partner.getName());
                partnerDTO.setDomain(partner.getDomain());
                partnerDTO.setAffiliateValue(partner.getAffiliatePercentage());
                partnerList.add(partnerDTO);
            }
        }
    }
}
