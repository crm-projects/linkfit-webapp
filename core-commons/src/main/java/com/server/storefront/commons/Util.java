package com.server.storefront.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.storefront.commons.constants.ApplicationConstants;
import com.server.storefront.commons.model.Profile;
import com.server.storefront.commons.model.CreatorProfile;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class Util {

    public static Profile generateUserProfile(String profile) {
        if (!StringUtils.hasText(profile))
            return null;
        return switch (profile) {
            case ApplicationConstants.CREATOR_PROFILE -> new CreatorProfile();
            default -> null;
        };
    }

    @SneakyThrows
    public static <T> String convertJSONToString(T object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @SneakyThrows
    public static <T> T convertStringToJSON(String jsonString, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
