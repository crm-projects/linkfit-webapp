package com.server.storefront.utils;

import com.server.storefront.utils.json.AbstractJsonResponse;
import com.server.storefront.utils.json.FullJsonResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Util {

    public static <T> AbstractJsonResponse<T> getJsonResponse(HttpServletRequest request, T content) {
        return new FullJsonResponse<T>(content);
    }

    public static LocalDateTime getExpiryPeriod(int limit) {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.plusDays(limit);
    }
}
