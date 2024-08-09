package com.server.storefront.commons.repository;

import com.server.storefront.commons.model.CreatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepository extends JpaRepository<CreatorProfile, String> {
}
