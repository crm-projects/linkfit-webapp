package com.server.storefront.controller;

import com.server.storefront.exception.CreatorProductException;
import com.server.storefront.service.CreatorProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AffiliateController {

    private final CreatorProductService productService;

    @GetMapping(value = "/share/{code}")
    public RedirectView getLongUrl(@PathVariable("code") String code, HttpServletRequest request) throws CreatorProductException {
        try {
            return productService.getLongUrlByCode(code, request);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CreatorProductException(ex.getMessage());
        }
    }
}
