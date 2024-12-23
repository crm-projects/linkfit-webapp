package com.server.storefront.utils;

import java.util.Set;
import java.util.regex.Pattern;

public class PathMatcherUtil {

    private PathMatcherUtil() {}

    private static final String PATH_VARIABLE_REPLACEMENT = "[^/]+";
    private static final String USER_NAME = "{user_name}";
    private static final String PRODUCT = "{p_id}";
    private static final String COLLECTION = "{collection_id}";

    public static boolean matchesPath(String requestPath, Set<String> allowedPaths) {
        for (String allowedPath : allowedPaths) {
            String regex = replaceWith(allowedPath);
            if (Pattern.matches(regex, requestPath)) {
                return true;
            }
        }
        return false;
    }

    private static String replaceWith(String allowedPath) {
        if (allowedPath.contains(USER_NAME)) {
            return allowedPath.replace(USER_NAME, PATH_VARIABLE_REPLACEMENT);
        } else if (allowedPath.contains(PRODUCT)) {
            return allowedPath.replace(PRODUCT, PATH_VARIABLE_REPLACEMENT);
        } else if (allowedPath.contains(COLLECTION)) {
            return allowedPath.replace(COLLECTION, PATH_VARIABLE_REPLACEMENT);
        } else {
            return allowedPath;
        }
    }
}
