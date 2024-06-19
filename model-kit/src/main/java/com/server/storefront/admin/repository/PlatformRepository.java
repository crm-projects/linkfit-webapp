package com.server.storefront.admin.repository;

import com.server.storefront.admin.model.Platform;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends AdminRepository<Platform, String> {
}
