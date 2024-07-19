package com.server.storefront.model.seller;

import com.server.storefront.Profile;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SellerProfile extends Profile {

    @Column(name = "BIO")
    private String bio;

    @Column(name = "IS_VERIFIED_IND")
    private boolean isVerifiedInd;

}
