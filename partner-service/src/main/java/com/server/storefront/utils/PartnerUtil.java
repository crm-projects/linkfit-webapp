package com.server.storefront.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class PartnerUtil {

    private static final String SUB_DOMAIN_SEPARATOR = "^(www\\.)?([^.]+)\\..*$";

    private static final String HOST_REPLACEMENT = "$2";

    public static String getDomainName(String url) {
        if (!StringUtils.hasLength(url)) {
            return url;
        }
        return removeSubdomain(url);
    }

    public static String removeSubdomain(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            return host.replaceAll(SUB_DOMAIN_SEPARATOR, HOST_REPLACEMENT);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
