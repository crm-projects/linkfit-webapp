package com.server.storefront.service;

import com.server.storefront.Util;
import com.server.storefront.constants.PartnerConstants;
import com.server.storefront.exception.CampaignException;
import com.server.storefront.exception.PartnerException;
import com.server.storefront.model.Campaign;
import com.server.storefront.model.Partner;
import com.server.storefront.repository.PartnerRepository;
import com.server.storefront.dto.CampaignDTO;
import com.server.storefront.dto.CreatorCriteriaDTO;
import com.server.storefront.dto.PartnerDTO;
import com.server.storefront.repository.CreatorCampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;

    private final CreatorCampaignRepository campaignRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAllPartnerDetails(int page, int limit) throws PartnerException {
        boolean hasNext;
        try {
            log.info("Fetching Partners List from the Database");
            PageRequest pageRequest = PageRequest.of(page, limit);
            Page<Partner> partners = partnerRepository.findAllByActiveInd(true, pageRequest);
            hasNext = (partners.getTotalPages() - 1) - page > 0;
            if (partners.isEmpty()) {
                throw new PartnerException(PartnerConstants.NO_INFO);
            }
            return scrubPartnerItems(partners, page, hasNext);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new PartnerException("");
        }
    }

    @Override
    @Transactional
    public CampaignDTO launchCampaign(CampaignDTO campaignDTO) throws CampaignException {
        try {
            if (Objects.isNull(campaignDTO)) {
                log.error("Null Object encountered");
                throw new CampaignException("Null Object encountered");
            }

            if (StringUtils.hasLength(campaignDTO.getPartnerId())) {
                Partner partner = partnerRepository.findById(campaignDTO.getPartnerId()).orElse(null);
                if (Objects.nonNull(partner)) {
                    return processCreatorCampaignItems(campaignDTO, partner);
                } else {
                    log.error("");
                    throw new PartnerException("");
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CampaignException(ex.getMessage());
        }
        return null;
    }

    private CampaignDTO processCreatorCampaignItems(CampaignDTO campaignDTO, Partner partner) {
        Campaign campaign = new Campaign();
        if (StringUtils.hasLength(campaignDTO.getId())) {
            campaign = campaignRepository.findById(campaignDTO.getId()).orElse(null);
            if (Objects.nonNull(campaign)) {
                createUpdateCampaignItems(campaign, campaignDTO, partner);
                return campaignDTO;
            }
        } else {
            campaign.setStartDate(new Date());
            createUpdateCampaignItems(campaign, campaignDTO, partner);
            return campaignDTO;
        }
        return null;
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
        if (CollectionUtils.isEmpty(partner.getCampaignList())) {
            partner.setCampaignList(Collections.singletonList(campaign));
        } else {
            partner.getCampaignList().add(campaign);
        }
        partnerRepository.save(partner);
    }

    @Override
    @Transactional(readOnly = true)
    public CampaignDTO getCampaignById(String campaignId) throws CampaignException {
        if (!StringUtils.hasLength(campaignId)) {
            throw new CampaignException("");
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
            throw new CampaignException("");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampaignDTO> getAllCampaignByPartnerId(String partnerId) throws PartnerException, CampaignException {
        if (!StringUtils.hasLength(partnerId)) {
            throw new PartnerException("");
        }
        try {
            List<CampaignDTO> campaignList = new ArrayList<>();
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
            } else {
                log.error("");
                throw new PartnerException("");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CampaignException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean deleteCampaignById(String campaignId) throws CampaignException {
        if (!StringUtils.hasLength(campaignId)) {
            throw new CampaignException("");
        }
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElse(null);
        if (Objects.nonNull(existingCampaign)) {
            existingCampaign.setActiveInd(false);
            campaignRepository.save(existingCampaign);
            return true;
        } else {
            throw new CampaignException("");
        }
    }

    private Map<String, Object> scrubPartnerItems(Page<Partner> partners, int page, boolean hasNext) {
        List<PartnerDTO> partnerList = new ArrayList<>();
        for (Partner partner : partners) {
            PartnerDTO partnerDTO = new PartnerDTO();
            partnerDTO.setName(partner.getName());
            partnerDTO.setDomain(partner.getDomain());
            partnerDTO.setAffiliateValue(partner.getAffiliatePercentage());
            partnerList.add(partnerDTO);
        }

        Map<String, Object> pageObject = new HashMap<>();
        pageObject.put("current_page", page);
        pageObject.put("has_next", hasNext);

        Map<String, Object> result = new HashMap<>();
        result.put("partners", partnerList);
        result.put("paging", pageObject);

        return result;
    }
}
