package com.server.storefront.repository;

import com.server.storefront.model.CreatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorProfileRepository extends JpaRepository<CreatorProfile, String> {

    boolean existsByUserName(String userName);
}
