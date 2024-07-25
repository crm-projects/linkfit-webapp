package com.server.storefront.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.storefront.creator.model.CreatorProfile;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "USER_BANK_DETAILS")
public class UserBankDetails {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @JsonProperty("accountNumber")
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @JsonProperty("bankIFSCCode")
    @Column(name = "IFSC_CODE")
    private String bankIFSCCode;

    @JsonProperty("holderName")
    @Column(name = "ACC_HOLDER_NAME")
    private String accountHolderName;

    @JsonProperty("panNumber")
    @Column(name = "PAN_DETAILS")
    private String permanentAccountNumber;

    @OneToOne
    @MapsId
    @JoinColumn(name = "USER_ID")
    private CreatorProfile creatorProfile;

}

