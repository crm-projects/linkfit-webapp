package com.server.storefront.handler;

import com.server.storefront.dto.ProductDTO;
import com.server.storefront.exception.PartnerException;
import org.springframework.http.ResponseEntity;

public interface BaseHandler {

    ResponseEntity<ProductDTO> getProductDetails(String productURL) throws PartnerException;

}
