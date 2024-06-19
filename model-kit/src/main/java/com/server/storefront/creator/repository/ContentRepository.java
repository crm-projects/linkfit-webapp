package com.server.storefront.creator.repository;

import com.server.storefront.creator.model.Content;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends CrudRepository<Content, String> {
}
