package com.server.storefront.creator.repository;

import com.server.storefront.creator.model.Creator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRepository extends CrudRepository<Creator, String> {

    @Query(value = "SELECT * FROM CREATOR WHERE CREATOR_EMAIL_ADDRESS = :email",nativeQuery = true)
    Optional<Creator> findCreatorByEmailAddress(String emailAddress);
}
