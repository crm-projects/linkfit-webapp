package com.server.storefront.handler;

import com.server.storefront.dto.ProductLite;
import com.server.storefront.exception.PartnerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public interface BaseHandler {

    ResponseEntity<ProductLite> getProductDetails(String productURL) throws PartnerException;

    String buildRedirectUrl(UriComponentsBuilder builder);

}
