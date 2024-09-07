package com.server.storefront.service;

import com.server.storefront.dto.CreatorProductDTO;
import com.server.storefront.dto.DummyProductDTO;
import com.server.storefront.exception.CreatorException;
import com.server.storefront.exception.CreatorProductException;
import com.server.storefront.exception.ProductException;

import java.util.Map;

public interface ProductService {

    Map<String, Object> addProduct(DummyProductDTO productDTO, String creatorId) throws ProductException;

    Map<String, Object> getAllProductsByCreator(String creatorId, int page, int limit) throws CreatorProductException;

    CreatorProductDTO getProductById(String productId) throws CreatorProductException, CreatorException;
}
