package com.server.storefront.repository;

import com.server.storefront.model.creator.Content;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends CrudRepository<Content, String> {
}
