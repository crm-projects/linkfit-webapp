package com.server.storefront.service;

import com.server.storefront.constants.*;
import com.server.storefront.dto.CollectionLite;
import com.server.storefront.dto.CreatorProductLite;
import com.server.storefront.dto.ProductLite;
import com.server.storefront.dto.ProductNode;
import com.server.storefront.exception.*;
import com.server.storefront.utils.HandlerUtil;
import com.server.storefront.handler.BaseHandler;
import com.server.storefront.model.*;
import com.server.storefront.model.Collection;
import com.server.storefront.repository.*;
import com.server.storefront.utils.PartnerUtil;
import com.server.storefront.utils.ProductUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatorProductServiceImpl implements CreatorProductService {

    private final HandlerUtil productHandlerFactory;

    private final CreatorRepository creatorRepository;

    private final CreatorProductRepository creatorProductRepository;

    private final ProductRepository productRepository;

    private final CollectionRepository collectionRepository;

    private final MediaRepository mediaRepository;

    private static final String PARTNER_HANDLER = "handler";
    private static final String PARTNER = "partner";
    private static final String TOTAL_COUNT = "total_records";
    private static final String HAS_NEXT = "has_next";
    private static final String PAGING = "paging";
    private static final String DATA = "data";
    private static final int BASE_CONDITION = 1;


    @Override
    @Transactional
    public Map<String, Object> addProduct(ProductNode productNode, String userName) throws CreatorException, InterruptedException, CreatorProductException {
        log.info("Adding products for {}", userName);

        if (productNode == null) {
            return new HashMap<>();
        }
        return checkProfileAndValidateProduct(productNode, userName);
    }

    private Map<String, Object> checkProfileAndValidateProduct(ProductNode productNode, String userName) throws CreatorException, CreatorProductException {

        CreatorProfile creator = creatorRepository.findByUserName(userName.strip())
                .orElseThrow(() -> new CreatorException(CreatorExceptionConstants.CREATOR_NOT_FOUND));

        Set<CreatorProduct> creatorProducts = processProductItems(productNode.getProducts(), productNode.isCollectionInd(), creator);

        Collection collection = productNode.isCollectionInd() ? scrubCollectionItems(productNode.getCollection(), creator.getId()) : null;

        return Optional.of(postProcessing(creatorProducts, collection)).get().orElse(new HashMap<>());

    }


    private Optional<Map<String, Object>> postProcessing(Set<CreatorProduct> products, Collection collection) {
        if (Objects.nonNull(collection)) {
            collection.setCreatorProducts(products);
            collectionRepository.save(collection);
        }

        List<CreatorProductLite> productList = new ArrayList<>();
        for (CreatorProduct p : products) {
            CreatorProductLite creatorProductDTO = new CreatorProductLite();
            creatorProductDTO.setId(p.getId());
            creatorProductDTO.setPid(p.getProduct().getProductId());
            creatorProductDTO.setTitle(p.getTitle());
            creatorProductDTO.setPrice(p.getPrice());
            creatorProductDTO.setImageURL(p.getImageUrl());
            creatorProductDTO.setCategory(p.getCategory());
            creatorProductDTO.setCurrency(p.getCurrency());
            creatorProductDTO.setExpiryDate(p.getAffiliateExpiresAt());
            creatorProductDTO.setCreatedTime(p.getCreatedTime());
            creatorProductDTO.setAffiliateUrl(ProductUtil.getAffiliateUrl(p.getAffiliateCode()));
            productList.add(creatorProductDTO);
        }

        Map<String, Object> productMap = new HashMap<>();
        productMap.put(ProductConstants.PRODUCTS, productList);

        if (collection != null) {
            productMap.put(CollectionConstants.COLLECTION, collection);
        }

        return Optional.of(productMap);
    }

    private Collection scrubCollectionItems(CollectionLite collectionLite, String creatorId) {
        Collection creatorCollection = new Collection();
        Optional.ofNullable(collectionLite).ifPresent((collection -> {
            creatorCollection.setName(collection.getName());
            creatorCollection.setDescription(collection.getDescription());
            creatorCollection.setImageURL(collection.getImageURL());
            creatorCollection.setActiveInd(true);
            creatorCollection.setCreatorId(creatorId);
            if (collection.isMediaInd()) {
                CollectionMedia collectionMedia = collection.getCollectionMedia();
                if (!StringUtils.hasText(collectionMedia.getId())) {
                    collectionMedia.setId(UUID.randomUUID().toString());
                    collectionMedia = mediaRepository.save(collectionMedia);
                }
                creatorCollection.setCollectionMedia(collectionMedia);
            }
        }));
        return creatorCollection;
    }

    private Set<CreatorProduct> processProductItems(List<String> products, boolean collectionInd, CreatorProfile creator) throws CreatorProductException {
        if (CollectionUtils.isEmpty(products)) {
            throw new CreatorProductException("Empty Product list provided. Please try again");
        }

        if (!collectionInd && products.size() > BASE_CONDITION) {
            throw new CreatorProductException("Multiple Products cannot be added without a Collection");
        }

        Set<CreatorProduct> productSet = new HashSet<>();
        for (String url : products) {
            try {
                CreatorProduct creatorProduct = sanitizeAndProcessCreatorItems(url, creator);
                productSet.add(creatorProduct);
            } catch (ProductException | HandlerException | PartnerException | CreatorProductException e) {
                log.error("Error while processing creator product", e);
                throw new CreatorProductException(e.getMessage());
            }
        }
        return productSet;
    }

    private CreatorProduct sanitizeAndProcessCreatorItems(String url, CreatorProfile creator) throws ProductException, HandlerException, PartnerException, CreatorProductException {
        if (!checkIfProductExists(url, creator.getId())) {
            ProductLite productLite = validateHandlerAndFetchProduct(url);
            Product product = validateProductAndSaveIfAbsent(productLite, url);
            return scrubAndSaveCreatorProduct(productLite, product, url, creator);
        } else throw new CreatorProductException("Product Already Exists");
    }

    private ProductLite validateHandlerAndFetchProduct(String url) throws HandlerException, ProductException, PartnerException {
        Map<String, Object> handlerParams = getPartnerHandler(url);
        if (!handlerParams.isEmpty()) {
            BaseHandler handler = (BaseHandler) handlerParams.get(PARTNER_HANDLER);
            String partner = (String) handlerParams.get(PARTNER);
            return retrieveProductDetailsFromPartner(handler, url, partner);
        }
        return new ProductLite();
    }

    private Map<String, Object> getPartnerHandler(String url) throws HandlerException {
        String partner = PartnerUtil.getDomainName(url);
        BaseHandler handler = productHandlerFactory.getHandler(partner);
        return Map.of(
                PARTNER, partner,
                PARTNER_HANDLER, handler
        );
    }

    private ProductLite retrieveProductDetailsFromPartner(BaseHandler handler, String url, String partner) throws ProductException, PartnerException {
        log.debug("Retrieve Product Details for {} from Partner - {}", url, partner);

        ResponseEntity<ProductLite> productDetails = handler.getProductDetails(url);
        if (productDetails.getBody() == null) {
            throw new ProductException(CreatorExceptionConstants.PRODUCT_DETAILS_NOT_FOUND);
        }

        productDetails.getBody().setPartnerName(partner);
        return productDetails.getBody();
    }

    private CreatorProduct scrubAndSaveCreatorProduct(ProductLite productLite, Product product, String url, CreatorProfile creator) throws ProductException {
        Date now = new Date();
        CreatorProduct creatorProduct = new CreatorProduct();
        creatorProduct.setActiveInd(true);
        creatorProduct.setCreatedBy(creator.getUserName());
        creatorProduct.setCreatedTime(now);
        creatorProduct.setCreator(creator);
        creatorProduct.setProduct(product);
        creatorProduct.setImageUrl(productLite.getImageUrl());
        creatorProduct.setPrice(productLite.getPrice());

        StringBuilder toHash = new StringBuilder(url);
        toHash.append(ProductConstants.PARAM_SEPARATOR_CHAR)
                .append(ProductConstants.SALT)
                .append(ProductConstants.PARAM_VALUE_CHAR)
                .append(creator.getUserName());
        String affiliateCode = ProductUtil.generateUniqueKey(toHash.toString(), false);
        if (StringUtils.hasLength(affiliateCode)) {
            productLite.setAffiliateCode(affiliateCode);
            setAffiliateDetails(affiliateCode, creatorProduct, now);
        }

        creatorProductRepository.save(creatorProduct);
        log.info("Product Successfully Added for Creator {}", creator.getUserName());
        return creatorProduct;
    }

    private void setAffiliateDetails(String code, CreatorProduct creatorProduct, Date now) {
        creatorProduct.setAffiliateCode(code);
        creatorProduct.setAffiliateExpiresAt(ProductUtil.validUntil(now));
    }

    Product validateProductAndSaveIfAbsent(ProductLite productLite, String url) throws ProductException {

        Product productObj = productRepository.findByProductId(productLite.getProductId()).orElse(null);

        if (productObj != null) {
            return productObj;
        }

        Product product = new Product();
        product.setProductId(productLite.getProductId());
        product.setProductURL(url);
        product.setUniqueKey(ProductUtil.generateUniqueKey(url, true));
        return productRepository.save(product);
    }

    private boolean checkIfProductExists(String url, String creatorId) throws ProductException {
        log.debug("Check if Product {} already Exists", url);
        String shortURL = ProductUtil.generateUniqueKey(url, true);
        Product product = productRepository.findByUniqueKey(shortURL).orElse(null);
        if (product != null) {
            CreatorProduct existingProduct = creatorProductRepository.findByProductId(product.getId(), creatorId).orElse(null);
            if (existingProduct != null) {
                log.info("Product Already Exists");
                return true;
            }
        }
        return false;
    }


    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAllProductsByCreator(String userName, int startIndex, int limit) throws CreatorProductException {

        try {
            List<CreatorProductLite> products = new ArrayList<>();
            CreatorProfile creator = creatorRepository.findByUserName(userName.strip())
                    .orElseThrow(() -> new CreatorException(CreatorExceptionConstants.CREATOR_NOT_FOUND));

            log.info("Fetching products for {}", creator.getUserName());

            /* TODO : Cache this result as product count won't increase at once */
            int totalCount = creatorProductRepository.fetchCount(creator.getId());

            List<CreatorProduct> creatorProducts = creatorProductRepository.findAllByCreatorId(creator.getId(), startIndex, limit);
            boolean hasNext = startIndex + creatorProducts.size() < totalCount;

            if (creatorProducts.isEmpty()) {
                throw new CreatorProductException(ProductConstants.NO_VALID_PRODUCTS);
            }

            creatorProducts.forEach(product -> {
                CreatorProductLite creatorProductDTO = new CreatorProductLite();
                creatorProductDTO.setId(product.getId());
                creatorProductDTO.setPid(product.getProduct().getProductId());
                creatorProductDTO.setImageURL(product.getImageUrl());
                creatorProductDTO.setAffiliateUrl(ProductUtil.getAffiliateUrl(product.getAffiliateCode()));
                creatorProductDTO.setPrice(product.getPrice());
                creatorProductDTO.setTitle(product.getTitle());
                creatorProductDTO.setCreatedTime(product.getCreatedTime());
                creatorProductDTO.setCurrency(product.getCurrency());
                creatorProductDTO.setCategory(product.getCategory());
                creatorProductDTO.setExpiryDate(product.getAffiliateExpiresAt());
                products.add(creatorProductDTO);
            });

            return enrichCreatorProductItems(products, totalCount, hasNext);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CreatorProductException(ex.getMessage());
        }
    }

    private Map<String, Object> enrichCreatorProductItems(List<CreatorProductLite> products, int totalCount, boolean hasNext) {
        return Map.of(
                DATA, products,
                PAGING, Map.of(
                        TOTAL_COUNT, totalCount,
                        HAS_NEXT, hasNext
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CreatorProductLite getProductById(String productId) throws CreatorProductException, CreatorException {

        CreatorProduct product = creatorProductRepository.findById(productId)
                .orElseThrow(() -> new CreatorProductException(ProductConstants.NO_VALID_PRODUCTS));

        CreatorProductLite creatorProductDTO = new CreatorProductLite();
        creatorProductDTO.setId(product.getId());
        creatorProductDTO.setPid(product.getProduct().getProductId());
        creatorProductDTO.setPrice(product.getPrice());
        creatorProductDTO.setAffiliateUrl(ProductUtil.getAffiliateUrl(product.getAffiliateCode()));
        creatorProductDTO.setTitle(product.getTitle());
        creatorProductDTO.setImageURL(product.getImageUrl());
        creatorProductDTO.setCreatedTime(product.getCreatedTime());
        creatorProductDTO.setCurrency(product.getCurrency());
        creatorProductDTO.setCategory(product.getCategory());
        creatorProductDTO.setExpiryDate(product.getAffiliateExpiresAt());
        return creatorProductDTO;
    }

    @Override
    @Transactional
    public boolean deleteProductById(String productId) throws CreatorProductException {
        if (!StringUtils.hasLength(productId)) {
            throw new CreatorProductException(ProductConstants.NO_VALID_PRODUCTS);
        }
        CreatorProduct product = creatorProductRepository.findById(productId).orElseThrow(() -> new CreatorProductException(ProductConstants.NO_VALID_PRODUCTS));
        product.setActiveInd(false);
        creatorProductRepository.save(product);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public RedirectView getLongUrlByCode(String code) throws CreatorProductException, HandlerException {
        if (!StringUtils.hasLength(code)) throw new CreatorProductException(ProductConstants.NO_VALID_CODE);

        CreatorProduct product = creatorProductRepository.findByAffiliateCode(code).orElseThrow(() -> new CreatorProductException(ProductConstants.NO_VALID_CODE));
        String baseProductUrl = product.getProduct().getProductURL();

        if (StringUtils.hasLength(baseProductUrl)) {
            String creator = product.getCreatedBy();
            if (StringUtils.hasLength(creator)) {
                /**
                 * Using {@link RedirectView}
                 */
                return processRedirectUrl(baseProductUrl);
            }
        }
        return null;
    }

    private RedirectView processRedirectUrl(String baseProductUrl) throws HandlerException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseProductUrl)
                .queryParam(AffiliateConstants.UTM_MEDIUM, AffiliateConstants.SOURCE)
                .queryParam(AffiliateConstants.UTM_SOURCE, AffiliateConstants.SOURCE)
                .queryParam(AffiliateConstants.PLATFORM_ID, AffiliateConstants.SOURCE);

        Map<String, Object> objectMap = getPartnerHandler(baseProductUrl);
        BaseHandler handler = (BaseHandler) objectMap.get(PARTNER_HANDLER);
        String redirectUrl = handler.buildRedirectUrl(builder);
        return new RedirectView(redirectUrl);
    }
}
