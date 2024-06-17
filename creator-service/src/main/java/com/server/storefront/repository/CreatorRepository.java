package com.server.storefront.repository;

import com.server.storefront.creator.Creator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepository extends CrudRepository<Creator, String> {
}
