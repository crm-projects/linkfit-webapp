package com.server.storefront.repository;

import com.server.storefront.commons.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorCampaignRepository extends JpaRepository<Campaign, String> {

}
