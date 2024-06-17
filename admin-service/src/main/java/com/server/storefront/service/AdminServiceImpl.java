package com.server.storefront.service;


import com.server.storefront.admin.Plan;
import com.server.storefront.admin.Platform;
import com.server.storefront.repository.PlanRepository;
import com.server.storefront.repository.PlatformRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    private static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

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

    public Platform savePlatformItems(Platform platform) {
        try {
            return platformRepository.save(platform);
        } catch (Exception ex ) {
            throw  new RuntimeException(ex.getMessage());
        }
    }
}
