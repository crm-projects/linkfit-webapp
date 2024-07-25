package com.server.storefront.store.model;

import com.server.storefront.seller.SellerProfile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.List;

@Data
@Entity
@Table(name = "SELLER_STORE")
@EqualsAndHashCode(callSuper = true)
public class SellerStore extends Store {

    @Column(name = "PHONE_NUMBER")
    private List<String> phoneNumber;

    @Column(name = "MAP_LOCATION")
    private String mapLocation;

    @Column(name = "GSTIN")
    private String gsTaxIdentificationNumber;

    @Column(name = "PRIMARY_ADDRESS_LINE")
    private String primaryAddressLine;

    @Column(name = "SECONDARY_ADDRESS_LINE")
    private String secondaryAddressLine;

    @Column(name = "PINCODE")
    private String pinCode;

    @Column(name = "LAST_MODIFIED_TIME")
    private Date lastModifiedTime;

    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_ID")
    private SellerProfile seller;

    @OneToMany(mappedBy = "store")
    private List<SellerProduct> productList;
}
