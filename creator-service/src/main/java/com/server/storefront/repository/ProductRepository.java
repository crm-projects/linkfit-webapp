package com.server.storefront.repository;

import com.server.storefront.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {

    Optional<Product> findByProductId(String productId);

    Optional<Product> findByUniqueKey(String shortURL);
}
