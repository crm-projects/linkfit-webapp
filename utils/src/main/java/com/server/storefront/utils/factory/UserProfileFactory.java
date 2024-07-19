package com.server.storefront.utils.factory;

import com.server.storefront.model.creator.CreatorProfile;
import com.server.storefront.Profile;
import com.server.storefront.model.seller.SellerProfile;
import org.springframework.util.StringUtils;

import static com.server.storefront.utils.ApplicationConstants.CREATOR_PROFILE;
import static com.server.storefront.utils.ApplicationConstants.SELLER_PROFILE;

public class UserProfileFactory {

    public Profile generateUserProfile(String profile) {
        if (!StringUtils.hasText(profile))
            return null;
        return switch (profile) {
            case CREATOR_PROFILE -> new CreatorProfile();
            case SELLER_PROFILE -> new SellerProfile();
            default -> null;
        };
    }
}
