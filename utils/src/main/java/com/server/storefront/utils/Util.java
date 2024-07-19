package com.server.storefront.utils;

import java.time.LocalDateTime;

public class Util {


    public static LocalDateTime getExpiryPeriod(int limit) {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.plusDays(limit);
    }
}
