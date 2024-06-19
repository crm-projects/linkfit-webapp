package com.server.storefront.creator.repository;

import com.server.storefront.creator.model.Script;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptRepository extends CrudRepository<Script, String> {
}
