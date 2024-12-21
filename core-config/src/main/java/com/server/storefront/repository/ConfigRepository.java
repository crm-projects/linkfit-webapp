package com.server.storefront.repository;

import com.server.storefront.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfigRepository extends JpaRepository<Config, String> {

    Config findByProperty(String property);
}
