package com.server.storefront.repository;

import com.server.storefront.model.MediaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<MediaData, String> {
}
