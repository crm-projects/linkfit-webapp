package com.server.storefront.repository;

import com.server.storefront.model.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, String> {

    Page<Partner> findAllByActiveInd(boolean activeInd, PageRequest pageRequest);

    boolean existsByName(String name);
}
