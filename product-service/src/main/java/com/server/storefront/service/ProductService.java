package com.server.storefront.service;

import com.server.storefront.dto.CreatorProductDTO;
import com.server.storefront.dto.ProductDTO;
import com.server.storefront.exception.CreatorException;
import com.server.storefront.exception.CreatorProductException;
import com.server.storefront.exception.ProductException;

import java.util.Map;

public interface ProductService {

    ProductDTO addProduct(String url, String creatorId) throws ProductException;

    Map<String, Object> getAllProductsByCreator(String creatorId, int page, int limit) throws CreatorProductException;

    CreatorProductDTO getProductById(String productId) throws CreatorProductException, CreatorException;
}
