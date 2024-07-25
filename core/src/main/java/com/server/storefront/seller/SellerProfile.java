package com.server.storefront.seller;

import com.server.storefront.Profile;
import com.server.storefront.creator.model.SellerPlanMapping;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "SELLER")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SellerProfile extends Profile {

    @Column(name = "BIO")
    private String bio;

    @Column(name = "VERIFIED_IND")
    private boolean isVerifiedInd;

    @Nonnull
    @OneToOne(mappedBy = "sellerProfile", cascade = CascadeType.ALL)
    private SellerPlanMapping sellerPlanMapping;

}
