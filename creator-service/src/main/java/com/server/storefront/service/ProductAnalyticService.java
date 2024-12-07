package com.server.storefront.service;

import com.server.storefront.dto.AnalyticsResponseLite;
import lombok.NonNull;

public interface ProductAnalyticService {

    AnalyticsResponseLite getAllProductsByCreator(String username, String category, String sortby, String order, int limit, int page);

    AnalyticsResponseLite getProductInsightById(@NonNull String id);
}
