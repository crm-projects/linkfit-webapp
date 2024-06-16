package com.server.storefront.utils.exception;

import lombok.Getter;

public class HttpServletException extends Exception {

    private String errorMessage;
    private String stackTrace;
    @Getter
    private int errorCode;
    public HttpServletException() { super(); }
    public HttpServletException(String errorMessage, int errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

}
