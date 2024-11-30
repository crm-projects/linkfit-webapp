package com.server.storefront.service;

import com.server.storefront.dto.CreatorProductLite;
import com.server.storefront.dto.ProductNode;
import com.server.storefront.exception.CreatorException;
import com.server.storefront.exception.CreatorProductException;
import com.server.storefront.exception.HandlerException;
import com.server.storefront.exception.ProductException;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

public interface CreatorProductService {

    Map<String, Object> addProduct(ProductNode productDTO, String userName) throws ProductException, CreatorException, InterruptedException, CreatorProductException;

    Map<String, Object> getAllProductsByCreator(String userName, int page, int limit) throws CreatorProductException;

    CreatorProductLite getProductById(String productId) throws CreatorProductException, CreatorException;

    boolean deleteProductById(String productId) throws CreatorProductException;

    RedirectView getLongUrlByCode(String code) throws CreatorProductException, HandlerException, HandlerException;
}
