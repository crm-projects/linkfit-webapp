package com.server.storefront.utils;

import java.util.HashSet;
import java.util.Set;

public class Path {

    private Path() { }

    private static final String GENERATE_OTP = "/auth/otp/generate";
    private static final String VALIDATE_AND_SIGN_UP = "/auth/otp/validate";
    private static final String LOGIN = "/auth/login";
    private static final String CHECK_USERNAME = "/auth/check/username";

    private static final String ACTUATOR = "/actuator";
    private static final String HEALTH = "/actuator/health";

    private static final String SWAGGER = "/v3/api-docs";
    private static final String SWAGGER_UI = "/swagger-ui/";

    public static Set<String> loadWhiteListedPaths() {
        Set<String> paths = new HashSet<>();
        paths.add(CHECK_USERNAME);
        paths.add(GENERATE_OTP);
        paths.add(LOGIN);
        paths.add(VALIDATE_AND_SIGN_UP);
        paths.add(ACTUATOR);
        paths.add(HEALTH);

        return paths;

    }

    public static Set<String> loadSwaggerPaths() {
        Set<String> paths = new HashSet<>();
        paths.add(SWAGGER);
        paths.add(SWAGGER_UI);

        return paths;
    }

}
