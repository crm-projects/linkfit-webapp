package com.server.storefront.service;


import com.server.storefront.dto.PartnerDTO;
import com.server.storefront.model.Partner;
import com.server.storefront.model.Plan;
import com.server.storefront.model.Platform;
import com.server.storefront.repository.PlanRepository;
import com.server.storefront.repository.PlatformRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {


    @Autowired
    PlanRepository planRepository;

    @Autowired
    PlatformRepository platformRepository;

    @Override
    @Transactional
    public boolean savePlanDetails(Plan plan) {
        try {
            planRepository.save(plan);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Platform savePlatformItems(Platform platform) {
        try {
            return platformRepository.save(platform);
        } catch (Exception ex ) {
            throw  new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Partner savePartnerItems(PartnerDTO partnerDTO) {
        try {

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return null;
    }
}
