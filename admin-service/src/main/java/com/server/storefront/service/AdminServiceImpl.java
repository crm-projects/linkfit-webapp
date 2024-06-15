package com.server.storefront.service;

import com.server.storefront.model.Plan;
import com.server.storefront.repository.PlanRepository;
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
}
