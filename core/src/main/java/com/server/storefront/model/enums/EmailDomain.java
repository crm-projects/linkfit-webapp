package com.server.storefront.model.enums;

public enum EmailDomain {

    GMAIL("@gmail.com");

    private String domain;

    EmailDomain(String s) {
        this.domain = s;
    }

    public String getDomain() {
        return domain;
    }
}
