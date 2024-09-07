package com.server.storefront.controller;

import com.server.storefront.dto.CreatorProductDTO;
import com.server.storefront.dto.ProductDTO;
import com.server.storefront.exception.CreatorProductException;
import com.server.storefront.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/{user_name}/products/add")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable("user_name") String userName,
                                                 @RequestBody String url,
                                                 HttpServletRequest request) throws CreatorProductException {
        try {
            return new ResponseEntity<>(productService.addProduct(url, userName), HttpStatus.OK);
        } catch (Exception e) {
            throw new CreatorProductException(e.getMessage());
        }
    }

    @GetMapping(value = "/{user_name}/products")
    public ResponseEntity<Map<String, Object>> getAllProductsByCreator(@PathVariable("user_name") String creatorId,
                                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                                       @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                                       HttpServletRequest request) throws CreatorProductException {
        try {
            return new ResponseEntity<>(productService.getAllProductsByCreator(creatorId, page, limit), HttpStatus.OK);
        } catch (Exception ex) {
            throw new CreatorProductException(ex.getMessage());
        }
    }

    @GetMapping(value = "/products/{pid}")
    public ResponseEntity<CreatorProductDTO> getCreatorProductById(@PathVariable(value = "pid") String productId, HttpServletRequest request) throws CreatorProductException {
        try {
            return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
        } catch (Exception ex) {
            throw new CreatorProductException(ex.getMessage());
        }
    }

}
