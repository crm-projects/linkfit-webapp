package com.server.storefront.repository;

import com.server.storefront.model.admin.Plan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends AdminRepository<Plan, String> {

    @Query(value = "SELECT * FROM PLAN WHERE NAME = :name", nativeQuery = true)
    Plan getPlanByName(@Param("name")String planName);

}
