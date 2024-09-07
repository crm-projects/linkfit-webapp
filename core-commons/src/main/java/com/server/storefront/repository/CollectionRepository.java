package com.server.storefront.repository;

import com.server.storefront.model.Collection;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CollectionRepository extends JpaRepository<Collection, String> {

    @Query(value = "SELECT c.ID as id, c.NAME as name, c.MEDIA_ID as media_id, c.IMAGE_URL as image_url, " +
            "c.DESCRIPTION as description, c.ACTIVE_IND as active_ind, " +
            "md.ID as m_id, md.MEDIA_ID as media_id, md.MEDIA_TYPE as media_type, " +
            "md.MEDIA_SOURCE as media_source, md.THUMBNAIL_URL as thumbnail_url, md.ACTIVE_IND as media_active_ind, " +
            "COUNT(p.ID) as product_count FROM COLLECTION c " +
            "JOIN COLLECTION_PRODUCT_MAPPING pc ON c.ID = pc.COLLECTION_ID " +
            "JOIN CREATOR_PRODUCT p ON pc.CREATOR_PRODUCT_ID = p.ID " +
            "LEFT JOIN MEDIA_DATA md ON c.MEDIA_ID = md.ID " +
            "WHERE p.CREATOR_ID = :creatorId AND c.ACTIVE_IND = true" +
            "GROUP BY c.ID",
            nativeQuery = true)
    Page<Tuple> findAllCollectionsByCreatorId(@Param("creatorId") String creatorId, Pageable pageable);





}
