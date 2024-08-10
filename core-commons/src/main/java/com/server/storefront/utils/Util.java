package com.server.storefront.utils;

import com.server.storefront.utils.constants.ApplicationConstants;
import com.server.storefront.utils.model.CreatorProfile;
import com.server.storefront.utils.model.Profile;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class Util {


    public static LocalDateTime getExpiryPeriod(int limit) {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.plusDays(limit);
    }

    public Profile generateUserProfile(String profile) {
        if (!StringUtils.hasText(profile))
            return null;
        return switch (profile) {
            case ApplicationConstants.CREATOR_PROFILE -> new CreatorProfile();
            default -> null;
        };
    }
}
