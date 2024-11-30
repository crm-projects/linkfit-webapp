package com.server.storefront.constants;

public class Path {

    private Path() { }

    public static final String GENERATE_OTP = "/auth/otp/generate";
    public static final String VALIDATE_AND_SIGN_UP = "/auth/otp/validate";
    public static final String LOGIN = "/auth/login";
    public static final String CHECK_USERNAME = "/auth/check/username";

    public static final String ACTUATOR = "/actuator";
    public static final String HEALTH = "/actuator/health";

    public static final String SWAGGER = "/v3/api-docs";
    public static final String SWAGGER_UI = "/swagger-ui/";

}
