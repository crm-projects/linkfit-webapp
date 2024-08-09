package com.server.storefront.commons;

import com.server.storefront.commons.constants.ApplicationConstants;
import com.server.storefront.commons.model.Profile;
import com.server.storefront.commons.model.CreatorProfile;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class Util {

    public static LocalDateTime getExpiryPeriod(int limit) {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.plusDays(limit);
    }

    public static Profile generateUserProfile(String profile) {
        if (!StringUtils.hasText(profile))
            return null;
        return switch (profile) {
            case ApplicationConstants.CREATOR_PROFILE -> new CreatorProfile();
            default -> null;
        };
    }
}
