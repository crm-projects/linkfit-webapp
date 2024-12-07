package com.server.storefront.repository;

import com.server.storefront.model.CreatorProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreatorProductRepository extends JpaRepository<CreatorProduct, String> {

    @Query(value = "SELECT * FROM CREATOR_PRODUCT WHERE CREATOR_ID=:creatorId AND PRODUCT_ID=:productId", nativeQuery = true)
    Optional<CreatorProduct> findByProductId(@Param("productId") String id, @Param("creatorId") String creatorId);

    Optional<CreatorProduct> findByAffiliateCode(String code);

    @Query(value = "SELECT COUNT(*) FROM CREATOR_PRODUCT WHERE CREATOR_ID=:creatorId", nativeQuery = true)
    int fetchCount(@Param("creatorId") String creatorId);

    @Query(value = "SELECT * FROM CREATOR_PRODUCT WHERE CREATOR_ID=:creatorId ORDER BY CREATED_TIME LIMIT :limit OFFSET :startIndex", nativeQuery = true)
    List<CreatorProduct> findAllByCreatorId(@Param("creatorId") String creatorId, @Param("startIndex") int startIndex, @Param("limit") int limit);

    @Query(value = """
            SELECT cp.ID FROM CREATOR_PRODUCT cp
            WHERE cp.created_by =:username AND cp.ACTIVE_IND = 1 AND cp.DRAFT_IND = 0
            """,nativeQuery = true)
    List<String> getProductIdsByUsername(@Param("username") String username);


}
