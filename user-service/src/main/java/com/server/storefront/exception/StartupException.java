package com.server.storefront.exception;

public class StartupException extends Exception {

    public StartupException(String message) {
        super(message);
    }

    public StartupException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
