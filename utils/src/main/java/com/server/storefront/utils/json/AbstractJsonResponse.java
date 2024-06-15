package com.server.storefront.utils.json;

public abstract class AbstractJsonResponse<T> {

    private T wrapped;

    protected AbstractJsonResponse(T wrapped) { this.wrapped = wrapped; }

    public T getWrapped() {
        return wrapped;
    }
}
