package com.server.storefront.repository;

import com.server.storefront.model.creator.Script;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptRepository extends CrudRepository<Script, String> {
}
