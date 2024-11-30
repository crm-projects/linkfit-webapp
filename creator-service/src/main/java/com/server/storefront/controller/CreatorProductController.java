package com.server.storefront.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.server.storefront.dto.CreatorProductLite;
import com.server.storefront.dto.ProductNode;
import com.server.storefront.exception.CreatorProductException;
import com.server.storefront.helper.Views;
import com.server.storefront.service.CreatorProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class CreatorProductController {

    private final CreatorProductService productService;

    @JsonView(Views.Public.class)
    @PostMapping("/{user_name}/products/add")
    public ResponseEntity<Map<String, Object>> addProducts(@PathVariable("user_name") String userName,
                                                           @RequestBody ProductNode productNode,
                                                           HttpServletRequest request) throws CreatorProductException {
        try {
            if (!StringUtils.hasLength(userName)) {
                throw new ServletException("Invalid user name provided");
            }
            return new ResponseEntity<>(productService.addProduct(productNode, userName), HttpStatus.OK);
        } catch (Exception e) {
            throw new CreatorProductException(e.getMessage());
        }
    }

    @GetMapping(value = "/{user_name}/products")
    public ResponseEntity<Map<String, Object>> getAllProductsByCreator(@PathVariable("user_name") String userName,
                                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                                       @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                                       HttpServletRequest request) throws CreatorProductException {
        try {
            return new ResponseEntity<>(productService.getAllProductsByCreator(userName, page, limit), HttpStatus.OK);
        } catch (Exception ex) {
            throw new CreatorProductException(ex.getMessage());
        }
    }

    @GetMapping(value = "/products/{pid}")
    public ResponseEntity<CreatorProductLite> getCreatorProductById(@PathVariable(value = "pid") String productId, HttpServletRequest request) throws CreatorProductException {
        try {
            return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
        } catch (Exception ex) {
            throw new CreatorProductException(ex.getMessage());
        }
    }

    @DeleteMapping(value = "/products/{pid}/delete")
    public ResponseEntity<Boolean> deleteCreatorProductById(@PathVariable(value = "pid") String productId, HttpServletRequest request) throws CreatorProductException {
        try {
            return new ResponseEntity<>(productService.deleteProductById(productId), HttpStatus.OK);
        } catch (Exception ex) {
            throw new CreatorProductException(ex.getMessage());
        }
    }

    @GetMapping(value = "/share/{code}")
    public RedirectView getLongUrl(@PathVariable("code") String code, HttpServletRequest request) throws CreatorProductException {
        try {
            return productService.getLongUrlByCode(code);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CreatorProductException(ex.getMessage());
        }
    }
}
