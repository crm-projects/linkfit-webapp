package com.server.storefront.utils.factory;

import com.server.storefront.creator.model.CreatorProfile;
import com.server.storefront.Profile;
import com.server.storefront.seller.SellerProfile;
import org.springframework.util.StringUtils;

import static com.server.storefront.utils.ApplicationConstants.CREATOR_PROFILE;
import static com.server.storefront.utils.ApplicationConstants.SELLER_PROFILE;

public class UserProfileFactory {

    public static Profile generateUserProfile(String profile) {
        if (!StringUtils.hasText(profile))
            return null;
        return switch (profile) {
            case CREATOR_PROFILE -> new CreatorProfile();
            case SELLER_PROFILE -> new SellerProfile();
            default -> null;
        };
    }
}
