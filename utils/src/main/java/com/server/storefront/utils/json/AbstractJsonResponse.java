package com.server.storefront.utils.json;

public abstract class AbstractJsonResponse<T> {

    private T responseData;

    protected AbstractJsonResponse(T responseData) { this.responseData = responseData; }

    public T getResponseData() {
        return responseData;
    }
}
