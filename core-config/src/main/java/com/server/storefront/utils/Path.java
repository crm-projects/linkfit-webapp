package com.server.storefront.utils;

import java.util.Set;

public class Path {

    private Path() { }

    private static final String GENERATE_OTP = "/auth/otp/generate";
    private static final String VALIDATE_AND_SIGN_UP = "/auth/otp/validate";
    private static final String LOGIN = "/auth/login";
    private static final String CHECK_USERNAME = "/auth/check/username";
    private static final String AFFILIATE = "/share";

    private static final String ACTUATOR = "/actuator";
    private static final String HEALTH = "/actuator/health";

    private static final String SWAGGER = "/v3/api-docs";
    private static final String SWAGGER_UI = "/swagger-ui/";

    private static final String WAITLIST = "/waitlist/signup";

    private static final String GET_CREATOR_PRODUCTS = "/users/{user_name}/products";
    private static final String GET_PRODUCT_DETAILS = "/users/products/{p_id}";
    private static final String GET_CREATOR_COLLECTIONS = "/users/{user_name}/collections";
    private static final String GET_COLLECTION_DETAILS = "/users/collections/{collection_id}";
    private static final String GET_CREATOR_PROFILE = "/{user_name}/meta";

    public static Set<String> loadWhiteListedPaths() {

        return Set.of(CHECK_USERNAME, GENERATE_OTP, LOGIN,
                VALIDATE_AND_SIGN_UP, ACTUATOR, HEALTH, WAITLIST, AFFILIATE);
    }

    public static Set<String> loadSwaggerPaths() {

        return Set.of(SWAGGER, SWAGGER_UI);
    }

    public static Set<String> loadSharedPaths() {

        return Set.of(GET_CREATOR_PRODUCTS, GET_PRODUCT_DETAILS, GET_CREATOR_COLLECTIONS,
                GET_COLLECTION_DETAILS, GET_CREATOR_PROFILE);
    }

}
