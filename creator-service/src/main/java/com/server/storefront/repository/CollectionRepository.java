package com.server.storefront.repository;

import com.server.storefront.model.Collection;
import com.server.storefront.model.CreatorProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CollectionRepository extends JpaRepository<Collection, String> {

    @Query(value = "SELECT COUNT(*) FROM COLLECTION WHERE CREATOR_ID=:creatorId", nativeQuery = true)
    int fetchCount(@Param("creatorId") String creatorId);

    @Query(value = "SELECT * FROM COLLECTION WHERE CREATOR_ID=:creatorId LIMIT :limit OFFSET :startIndex", nativeQuery = true)
    List<Collection> findAllByCreatorId(@Param("creatorId") String creatorId, @Param("startIndex") int startIndex, @Param("limit") int limit);

}
