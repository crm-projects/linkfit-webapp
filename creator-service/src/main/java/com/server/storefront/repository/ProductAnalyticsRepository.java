package com.server.storefront.repository;

import com.server.storefront.model.ProductAnalytics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAnalyticsRepository extends JpaRepository<ProductAnalytics, String> {

    boolean existsByProductId(String productId);

    @Query(value = """
            SELECT pa.* FROM PRODUCT_ANALYTICS pa
            WHERE pa.CREATOR_PRODUCT_ID =:id
            """,nativeQuery = true)
    ProductAnalytics getInsightById(@Param("id") String id);


    @Query(value = """
            SELECT * FROM PRODUCT_ANALYTICS pa
            WHERE pa.CREATOR_PRODUCT_ID IN(:products)
            AND (:category = 'all' OR pa.category =:category)
            """,
            nativeQuery = true)
    Page<ProductAnalytics> getProductsByCategory(
            @Param("products") List<String> creatorProductsIdList,
            @Param("category") String category,
            Pageable pageable);
}
