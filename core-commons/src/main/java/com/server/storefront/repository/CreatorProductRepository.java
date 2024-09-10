package com.server.storefront.repository;

import com.server.storefront.model.CreatorProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorProductRepository extends JpaRepository<CreatorProduct, String> {

    @Query(value = "SELECT * FROM CREATOR_PRODUCT WHERE CREATOR_ID=:creatorId AND PRODUCT_ID=:productId", nativeQuery = true)
    Optional<CreatorProduct> findByProductId(@Param("productId") String id, @Param("creatorId") String creatorId);

    Page<CreatorProduct> findAllByCreatorId(String creatorId, Pageable pageable);

    Optional<CreatorProduct> findByAffiliateCode(String code);
}
