package com.server.storefront.utils.repository;

import com.server.storefront.utils.model.CreatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepository extends JpaRepository<CreatorProfile, String> {
}

