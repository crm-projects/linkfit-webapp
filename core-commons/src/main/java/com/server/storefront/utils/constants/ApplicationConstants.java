package com.server.storefront.utils.constants;

public class ApplicationConstants {

    public static final String STOREFRONT_USER = "X-STOREFRONT-USER";
    public static final String USER_PROFILE = "userProfile";
    public static final String CREATOR_PROFILE = "CREATOR";
    public static final String HASH = "SHA-256";
    public static final String PAN_VALIDATION = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
    public static final String IFSC_VALIDATION = "^[A-Z]{4}0[A-Z0-9]{6}$";
    public static final String ACCOUNT_NUMBER_VALIDATION = "^[0-9]{9,18}$";
    public static final String OTP_AUTH_SUBJECT = "Storefront Email Verification Code";

}
