package com.server.storefront.utils.json;

public class FullJsonResponse<T> extends AbstractJsonResponse<T>{
    public FullJsonResponse(T responseData) {
        super(responseData);
    }
}
