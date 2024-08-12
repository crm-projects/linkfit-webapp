package com.server.storefront.commons.repository;

import com.server.storefront.commons.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, String> {

    List<Partner> findAllByActiveInd(boolean activeInd);
}
