package com.server.storefront.repository;

import com.server.storefront.model.Click;
import com.server.storefront.model.ClickInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickInsightRepository extends JpaRepository<ClickInsight, String> {

    boolean existsByClicksAndIpAddressAndUserAgent(Click clicks, String ipAddress, String userAgent);

    @Query(value = "SELECT COUNT(*) FROM ClickInsights ci1 " +
            "WHERE ci1.clicks.id = :clickId " +
            "AND NOT EXISTS (SELECT 1 FROM ClickInsights ci2 " +
            "WHERE ci2.clicks.id = :clickId " +
            "AND ci2.ipAddress = ci1.ipAddress " +
            "AND ci2.userAgent = ci1.userAgent " +
            "AND ci2.id < ci1.id)", nativeQuery = true)
    int countUniqueClicks(@Param("clickId") String clickId);
}
