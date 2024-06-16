package com.server.storefront.utils.holder;

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
