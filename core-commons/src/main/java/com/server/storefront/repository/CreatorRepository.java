package com.server.storefront.repository;

import com.server.storefront.model.CreatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRepository extends JpaRepository<CreatorProfile, String> {

    Optional<CreatorProfile> findByUserName(String userName);
}
