package com.server.storefront.service;

import com.server.storefront.dto.CreatorProductDTO;
import com.server.storefront.dto.DummyProductDTO;
import com.server.storefront.exception.CreatorException;
import com.server.storefront.exception.CreatorProductException;
import com.server.storefront.exception.ProductException;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

public interface ProductService {

    Map<String, Object> addProduct(DummyProductDTO productDTO, String userName) throws ProductException;

    Map<String, Object> getAllProductsByCreator(String creatorId, int page, int limit) throws CreatorProductException;

    CreatorProductDTO getProductById(String productId) throws CreatorProductException, CreatorException;

    RedirectView getLongUrlByCode(String code) throws CreatorProductException;
}
