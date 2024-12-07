package com.server.storefront.service;

import com.server.storefront.constants.CreatorExceptionConstants;
import com.server.storefront.dto.AnalyticsResponseLite;
import com.server.storefront.dto.CreatorProductResponseLite;
import com.server.storefront.dto.PartnerResponseLite;
import com.server.storefront.enums.Order;
import com.server.storefront.exception.AnalyticsException;
import com.server.storefront.exception.CreatorException;
import com.server.storefront.exception.PartnerException;
import com.server.storefront.model.Click;
import com.server.storefront.model.CreatorProfile;
import com.server.storefront.model.Partner;
import com.server.storefront.model.ProductAnalytics;
import com.server.storefront.repository.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static ch.qos.logback.core.util.StringUtil.capitalizeFirstLetter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAnalyticServiceImpl implements ProductAnalyticService {

    private final CreatorRepository creatorRepository;

    private final ProductAnalyticsRepository analyticsRepository;

    private final CreatorProductRepository creatorProductRepository;

    private final PartnerRepository partnerRepository;

    private final ClickRepository clickRepository;


    @Override
    @Transactional(readOnly = true)
    public AnalyticsResponseLite getAllProductsByCreator(String username, String category, String sortby, String order, int limit, int page) {
        log.info("Retrieve products list for {} for category {}", username, category);
        try {
            CreatorProfile profile = creatorRepository.findByUserName(username)
                    .orElseThrow(() -> new CreatorException(CreatorExceptionConstants.CREATOR_NOT_FOUND));

            List<String> creatorProductsIdList = getProductIdsByUsername(username);

            if (creatorProductsIdList.isEmpty()) {
                log.error("user doesn't have any active products");
                return null;
            }

            Sort sort;
            if (order.equalsIgnoreCase(Order.D.toString())) {
                sort = Sort.by(sortby).descending();
            } else if (order.equalsIgnoreCase(Order.A.toString())) {
                sort = Sort.by(sortby).ascending();
            } else if (order.isBlank()) {
                sort = Sort.unsorted();
            } else throw new AnalyticsException("Invalid order value: " + order);

            Pageable pageable = PageRequest.of(page, limit, sort);

            Page<ProductAnalytics> productPage = analyticsRepository.getProductsByCategory(creatorProductsIdList, category, pageable);
            boolean hasNextPage = productPage.hasNext();

            List<ProductAnalytics> filteredProducts = productPage.getContent();

            log.info("With pagination {} and page details current page: {} and has Next {} and total size is {}", filteredProducts, pageable.getPageNumber(), hasNextPage, pageable.getPageSize());

            Set<String> partners = new HashSet<>();
            List<CreatorProductResponseLite> products = new ArrayList<>();

            filteredProducts.forEach(p -> {
                CreatorProductResponseLite product = mapProductDetails(p);
                product.setPartnerId(p.getPartnerId());
                partners.add(p.getPartnerId());
                products.add(product);
            });

            List<PartnerResponseLite> partnerResponseDtos = new ArrayList<>();
            if (!partners.isEmpty()) {
                for (String id : partners) {
                    PartnerResponseLite partnerResponseDto = mapPartnerDetails(id);
                    partnerResponseDtos.add(partnerResponseDto);
                }
            } else log.error(" No Partner Details found for retrieved products {}", products);

            Map<String, Object> pagination = Map.of("hasNextPage", hasNextPage, "currentPage", productPage.getNumber());
            return new AnalyticsResponseLite(partnerResponseDtos, products, pagination);
        } catch (Exception e) {
            log.error("Exception occured while retriving product List");
            throw new AnalyticsException(e.getMessage());
        }
    }

    @Cacheable(value = "activeProductList", key = "#username")
    public List<String> getProductIdsByUsername(String username) {
        return creatorProductRepository.getProductIdsByUsername(username);
    }

    private PartnerResponseLite mapPartnerDetails(@NonNull String id) throws PartnerException {
        log.debug("retrieve Partner Details");
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerException("No Partner Found with this id " + id));

        return new PartnerResponseLite(
                partner.getId(),
                partner.getName(),
                capitalizeFirstLetter(partner.getName()),
                partner.getImageUrl()
        );
    }

    private AnalyticsResponseLite mapProductAndClick(Click clicks, ProductAnalytics analytics) throws PartnerException {
        log.debug("Mapping product and click for product Insight");
        CreatorProductResponseLite productInsightDto = mapProductDetails(analytics);
        productInsightDto.setCategory(analytics.getCategory());
        if (clicks != null) {
            productInsightDto.setTotalClick(clicks.getTotalClicks());
            productInsightDto.setUniqueClick(clicks.getUniqueClicks());
        }
        PartnerResponseLite partnerResponseDto = mapPartnerDetails(analytics.getPartnerId());
        return new AnalyticsResponseLite(productInsightDto, partnerResponseDto);
    }

    private static CreatorProductResponseLite mapProductDetails(ProductAnalytics analytics) {
        return new CreatorProductResponseLite(
                analytics.getProductId(),
                analytics.getTitle(),
                analytics.getPrice(),
                analytics.getRevenue(),
                analytics.getUnitSold()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticsResponseLite getProductInsightById(@NonNull String id) {
        log.info("Get product Insight for id: {}", id);
        try {
            if (analyticsRepository.existsByProductId(id)) {
                ProductAnalytics analytics = analyticsRepository.getInsightById(id);
                Click clicks = clickRepository.findByCreatorProductId(id).orElse(null);
                return mapProductAndClick(clicks, analytics);
            } else throw new CreatorException(CreatorExceptionConstants.PRODUCT_DETAILS_NOT_FOUND);

        } catch (Exception e) {
            log.error("error while doing map");
            throw new AnalyticsException(e.getMessage());
        }

    }
}
