package com.server.storefront.repository;

import com.server.storefront.model.Platform;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends AdminRepository<Platform, String> {
}
