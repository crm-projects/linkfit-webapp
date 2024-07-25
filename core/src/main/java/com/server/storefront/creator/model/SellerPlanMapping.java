package com.server.storefront.creator.model;

import com.server.storefront.admin.model.Plan;
import com.server.storefront.seller.SellerProfile;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
@Data
@Entity
@Table(name = "SELLER_PLAN_MAPPING")
public class SellerPlanMapping {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @OneToOne
    @JoinColumn(name = "SELLER_ID")
    private SellerProfile sellerProfile;

    @OneToOne
    @JoinColumn(name = "PLAN_ID")
    private Plan plan;

    @Column(name = "ACTIVE_IND")
    private boolean activeInd;

    @Column(name = "IS_FREE_TRIAL")
    private boolean isFreeTrial;

    @Column(name = "EXPIRES_ON")
    private LocalDateTime expiresOn;
}
