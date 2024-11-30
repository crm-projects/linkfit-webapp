package com.server.storefront.service;

import com.server.storefront.Util;
import com.server.storefront.constants.PartnerConstants;
import com.server.storefront.exception.CampaignException;
import com.server.storefront.exception.PartnerException;
import com.server.storefront.model.Campaign;
import com.server.storefront.model.Partner;
import com.server.storefront.repository.PartnerRepository;
import com.server.storefront.dto.CampaignLite;
import com.server.storefront.dto.CriteriaLite;
import com.server.storefront.dto.PartnerLite;
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
    public CampaignLite launchCampaign(CampaignLite campaignLite) throws CampaignException {
        try {
            if (Objects.isNull(campaignLite)) {
                log.error("Null Object encountered");
                throw new CampaignException("Null Object encountered");
            }

            if (StringUtils.hasLength(campaignLite.getPartnerId())) {
                Partner partner = partnerRepository.findById(campaignLite.getPartnerId()).orElse(null);
                if (Objects.nonNull(partner)) {
                    return processCreatorCampaignItems(campaignLite, partner);
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

    private CampaignLite processCreatorCampaignItems(CampaignLite campaignLite, Partner partner) {
        Campaign campaign = new Campaign();
        if (StringUtils.hasLength(campaignLite.getId())) {
            campaign = campaignRepository.findById(campaignLite.getId()).orElse(null);
            if (Objects.nonNull(campaign)) {
                createUpdateCampaignItems(campaign, campaignLite, partner);
                return campaignLite;
            }
        } else {
            campaign.setStartDate(new Date());
            createUpdateCampaignItems(campaign, campaignLite, partner);
            return campaignLite;
        }
        return null;
    }

    private void createUpdateCampaignItems(Campaign campaign, CampaignLite campaignLite, Partner partner) {
        campaign.setTitle(campaignLite.getTitle());
        campaign.setDescription(campaign.getDescription());
        campaign.setCommissionRateForPlatform(0);
        campaign.setCommissionRateForCreator(campaignLite.getCommissionRateForCreator());
        campaign.setEndDate(campaignLite.getEndDate());
        campaign.setCreatorCriteria(Util.convertJSONToString(campaignLite.getCreatorCriteria()));
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
    public CampaignLite getCampaignById(String campaignId) throws CampaignException {
        if (!StringUtils.hasLength(campaignId)) {
            throw new CampaignException("");
        }
        Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
        if (Objects.nonNull(campaign)) {
            CampaignLite campaignLite = new CampaignLite();
            campaignLite.setTitle(campaign.getTitle());
            campaignLite.setDescription(campaign.getDescription());
            campaignLite.setCommissionRateForCreator(campaign.getCommissionRateForCreator());
            campaignLite.setStartDate(campaign.getStartDate());
            campaignLite.setEndDate(campaign.getEndDate());
            campaignLite.setActiveInd(campaign.isActiveInd());
            CriteriaLite criteriaLite = Util.convertStringToJSON(campaign.getCreatorCriteria(), CriteriaLite.class);
            campaignLite.setCreatorCriteria(criteriaLite);
            return campaignLite;
        } else {
            log.error("");
            throw new CampaignException("");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampaignLite> getAllCampaignByPartnerId(String partnerId) throws PartnerException, CampaignException {
        if (!StringUtils.hasLength(partnerId)) {
            throw new PartnerException("");
        }
        try {
            List<CampaignLite> campaignList = new ArrayList<>();
            Partner partner = partnerRepository.findById(partnerId).orElse(null);
            if (Objects.nonNull(partner)) {
                List<Campaign> campaigns = partner.getCampaignList();
                if (!CollectionUtils.isEmpty(campaigns)) {
                    for (Campaign campaign : campaigns) {
                        CampaignLite campaignLite = new CampaignLite();
                        campaignLite.setTitle(campaign.getTitle());
                        campaignLite.setDescription(campaign.getDescription());
                        campaignLite.setStartDate(campaign.getStartDate());
                        campaignLite.setEndDate(campaign.getEndDate());
                        campaignLite.setCommissionRateForCreator(campaign.getCommissionRateForCreator());
                        campaignLite.setActiveInd(campaign.isActiveInd());
                        campaignLite.setCreatorCriteria(Util.convertStringToJSON(campaign.getCreatorCriteria(), CriteriaLite.class));
                        campaignList.add(campaignLite);
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
        List<PartnerLite> partnerList = new ArrayList<>();
        for (Partner partner : partners) {
            PartnerLite partnerLite = new PartnerLite();
            partnerLite.setName(partner.getName());
            partnerLite.setDomain(partner.getDomain());
            partnerLite.setAffiliateValue(partner.getAffiliatePercentage());
            partnerList.add(partnerLite);
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
