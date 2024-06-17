package com.server.storefront.enums;

public enum PlanControl {

    BASE_PLAN(7);

    private int expiryDate;

    PlanControl(int days) {
        this.expiryDate = days;
    }

    public int getExpiryDate() {
        return expiryDate;
    }
}
