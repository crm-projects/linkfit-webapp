package com.server.storefront.repository;

import com.server.storefront.model.CollectionMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<CollectionMedia, String> {
}
