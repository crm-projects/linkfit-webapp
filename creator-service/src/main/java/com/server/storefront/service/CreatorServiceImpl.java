package com.server.storefront.service;

import com.server.storefront.model.*;
import com.server.storefront.repository.AdminRepository;
import com.server.storefront.repository.CreatorRepository;
import com.server.storefront.repository.PlanRepository;
import com.server.storefront.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CreatorServiceImpl implements CreatorService {

    @Autowired
    CreatorRepository creatorRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    PlatformRepository platformRepository;

    @Override
    @Transactional
    public boolean saveUpdateCreatorDetails(CreatorLite creatorLite) {
        try {
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Creator saveUpdatePlatformDetails(CreatorLite creatorLite) {
        try {
            Creator creator = new Creator();
            creator.setUserName(creatorLite.getUserName());
            creator.setEmailAddress(creatorLite.getEmailAddress());
            creator.setGender(creatorLite.getGender());
            if (creatorLite.getPlan() != null) {
                if (StringUtils.hasLength(creatorLite.getPlan().getPlanId())) {
                    Optional<Plan> planObj = planRepository.findById(creatorLite.getPlan().getPlanId());
                    CreatorPlanMapping creatorPlanMapping = new CreatorPlanMapping();
                    planObj.ifPresent(creatorPlanMapping::setPlan);
                    creatorPlanMapping.setCreator(creator);
                    creatorPlanMapping.setActiveInd(creatorLite.getPlan().isActiveInd());
                    creatorPlanMapping.setFreeTrial(creatorLite.getPlan().isFreeTrial());
                }
            }
            if(creatorLite.getPlatformsList() != null) {
                for(PlatformLite platformLite : creatorLite.getPlatformsList()) {
                    Optional<Platform> platformObj = platformRepository.findById(platformLite.getPlatformId());
                    CreatorSocialMediaMapping socialMediaMapping = new CreatorSocialMediaMapping();
                    socialMediaMapping.setCreator(creator);
                    socialMediaMapping.setPlatform(platformObj.get());
                    socialMediaMapping.setLinkedInd(platformLite.isLinkedInd());
                    socialMediaMapping.setActiveInd(platformLite.isActiveInd());
                }
            }
            return creatorRepository.save(creator);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
