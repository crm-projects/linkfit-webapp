package com.server.storefront.repository;

import com.server.storefront.model.Plan;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends AdminRepository<Plan, String> {

}
