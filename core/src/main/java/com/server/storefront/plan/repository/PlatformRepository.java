package com.server.storefront.plan.repository;

import com.server.storefront.admin.AdminRepository;
import com.server.storefront.admin.model.Platform;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends AdminRepository<Platform, String> {
}
