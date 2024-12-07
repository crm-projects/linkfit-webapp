package com.server.storefront.repository;

import com.server.storefront.model.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClickRepository extends JpaRepository<Click, String> {

    Optional<Click> findByCreatorProductId(String productId);
}
