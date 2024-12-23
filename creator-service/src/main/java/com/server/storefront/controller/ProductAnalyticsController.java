package com.server.storefront.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.server.storefront.dto.AnalyticsResponseLite;
import com.server.storefront.exception.AnalyticsException;
import com.server.storefront.helper.Views;
import com.server.storefront.service.ProductAnalyticService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class ProductAnalyticsController {

    private final ProductAnalyticService analyticService;

    private static final String INVALID_INPUT = "Input should not be null. Provide a Valid Input";

    @JsonView(Views.Public.class)
    @GetMapping("/{user_name}/products/analytics")
    public ResponseEntity<AnalyticsResponseLite> getAllProductsByUsername(@PathVariable("user_name") @NonNull String username,
                                                                          @RequestParam(value = "category", required = false, defaultValue = "all") String category,
                                                                          @RequestParam(value = "sortby", required = false, defaultValue = "revenue") String sortby,
                                                                          @RequestParam(value = "order", required = false, defaultValue = "d") String order,
                                                                          @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                                          @RequestParam(value = "page", defaultValue = "0") int page) {

        if (!StringUtils.hasText(username) || !StringUtils.hasText(category) || !StringUtils.hasText(sortby) ||
                !StringUtils.hasText(order)) {
            log.error(INVALID_INPUT);
            throw new AnalyticsException(INVALID_INPUT);
        }

        if (limit <= 0 || page < 0) {
            throw new AnalyticsException(" Pagination input must be greater than 0");
        }

        try {
            AnalyticsResponseLite response = analyticService.getAllProductsByCreator(username, category, sortby, order, limit, page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AnalyticsException e) {
            log.error("Analytics error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage(), e);
            throw new AnalyticsException("An unexpected error occurred while fetching analytics." + e.getMessage());
        }

    }


    @JsonView(Views.Analytics.class)
    @GetMapping("/product/{p_id}/analytics")
    public ResponseEntity<AnalyticsResponseLite> getProductInsightsById(@PathVariable("p_id") @NonNull String id) {

        if (!StringUtils.hasText(id)) {
            log.error(INVALID_INPUT);
            throw new AnalyticsException(INVALID_INPUT);
        }
        try {
            AnalyticsResponseLite analyticsDto = analyticService.getProductInsightById(id);
            return new ResponseEntity<>(analyticsDto, HttpStatus.OK);
        } catch (AnalyticsException e) {
            log.error("Analytics error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred for product id {} error is {}", id, e.getMessage(), e);
            throw new AnalyticsException("An unexpected error occurred while fetching analytics ." + e.getMessage());
        }

    }
}
