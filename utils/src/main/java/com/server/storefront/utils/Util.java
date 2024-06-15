package com.server.storefront.utils;

import com.server.storefront.utils.json.AbstractJsonResponse;
import com.server.storefront.utils.json.FullJsonResponse;
import jakarta.servlet.http.HttpServletRequest;

public class Util {

    public static <T> AbstractJsonResponse<T> getJsonResponse(HttpServletRequest request, T content) {
        return new FullJsonResponse<T>(content);
    }
}
