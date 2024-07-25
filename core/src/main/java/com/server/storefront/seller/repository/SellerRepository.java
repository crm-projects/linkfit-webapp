package com.server.storefront.seller.repository;

import com.server.storefront.seller.SellerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<SellerProfile, String> {
}
