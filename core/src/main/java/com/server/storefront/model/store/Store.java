package com.server.storefront.model.store;

import com.server.storefront.model.seller.Seller;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Entity
@Table(name = "STORE")
@NoArgsConstructor
public class Store {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE_NUMBER")
    private List<String> phoneNumber;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

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

    @Column(name = "IS_VERIFIED_IND")
    private boolean isVerifiedInd;

    @Column(name = "CURRENCY_TYPE")
    private String currencyType;

    @Column(name = "IS_ACTIVE_IND")
    private boolean isActiveInd;

    @Column(name = "LAST_MODIFIED_TIME")
    private Date lastModifiedTime;

    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @Column(name = "CREATED_BY_TIME")
    private Date createdByTime;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_ID")
    private Seller seller;

    @OneToMany(mappedBy = "store")
    private List<Product> productList;


}
