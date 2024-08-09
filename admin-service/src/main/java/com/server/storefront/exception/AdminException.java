package com.server.storefront.exception;

public class AdminException extends RuntimeException {

    public AdminException() { super(); };

    public AdminException(String message) { super(message); }
}
