package com.server.storefront.service;

import com.server.storefront.constants.*;
import com.server.storefront.dto.CollectionLite;
import com.server.storefront.dto.CreatorProductLite;
import com.server.storefront.dto.ProductLite;
import com.server.storefront.dto.ProductNodeDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    private static final String CURRENT_PAGE = "current_page";
    private static final String HAS_NEXT = "has_next";
    private static final String PAGING = "paging";
    private static final String DATA = "data";
    private static final int BASE_CONDITION = 1;


    @Override
    @Transactional
    public Map<String, Object> addProduct(ProductNodeDTO productNode, String userName) throws ProductException {
        log.info("Adding products for {}", userName);
        try {
            return checkProfileAndValidateProduct(productNode, userName);
        } catch (Exception e) {
            throw new ProductException(e.getMessage());
        }
    }

    private Map<String, Object> checkProfileAndValidateProduct(ProductNodeDTO productNode, String userName) throws CreatorException, InterruptedException {
        CreatorProfile creator = creatorRepository.findByUserName(userName.strip()).orElseThrow(() -> new CreatorException(CreatorExceptionConstants.CREATOR_NOT_FOUND));

        Map<String, Object> productResponse = new HashMap<>();
        if (Objects.nonNull(productNode)) {
            CompletableFuture<Set<CreatorProduct>> validateProductAndSaveIfAbsent = CompletableFuture.supplyAsync(() -> {
                try {
                    return processProductItems(productNode.getProducts(), productNode.isCollectionInd(), creator);
                } catch (CreatorProductException e) {
                    throw new RuntimeException(e);
                }
            });
            CompletableFuture<Collection> validateCollection = CompletableFuture.supplyAsync(() -> {
                if (productNode.isCollectionInd())
                    return scrubCollectionItems(productNode.getCollection(), creator.getId());
                return null;
            });
            CompletableFuture<Optional<Map<String, Object>>> response = validateProductAndSaveIfAbsent.thenCombine(validateCollection, this::preparePostProcessing);

            try {
                if(response.get().isPresent()) {
                    productResponse = response.get().get();
                }

            } catch (InterruptedException | ExecutionException ex) {
                throw new InterruptedException("Error while processing creator products and collection");
            }
        }
        return productResponse;
    }

    private Optional<Map<String, Object>> preparePostProcessing(Set<CreatorProduct> products, Collection collection) {
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

        return Optional.of(Map.of(
                ProductConstants.PRODUCTS, productList,
                CollectionConstants.COLLECTION, collection
        ));
    }

    private Collection scrubCollectionItems(CollectionLite CollectionLite, String creatorId) {
        Collection creatorCollection = new Collection();
        Optional.ofNullable(CollectionLite).ifPresent((collection -> {
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
        if (CollectionUtils.isEmpty(products)) return Collections.emptySet();

        if(!collectionInd && products.size() > BASE_CONDITION ) { throw new CreatorProductException("Multiple Products cannot be added without a Collection");}
        Set<CreatorProduct> productSet = new HashSet<>();
        for (String url : products) {
            try {
                CreatorProduct creatorProduct = sanitizeAndProcessCreatorItems(url, creator);
                productSet.add(creatorProduct);
            } catch (ProductException | HandlerException | PartnerException | CreatorProductException e) {
                log.error("Error while processing creator product", e);
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
        if (Objects.nonNull(productDetails) && Objects.nonNull(productDetails.getBody())) {
            productDetails.getBody().setPartnerName(partner);
            return productDetails.getBody();
        } else throw new ProductException(CreatorExceptionConstants.PRODUCT_DETAILS_NOT_FOUND);
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
        toHash.append(ProductConstants.PARAM_SEPARATOR_CHAR).append(ProductConstants.SALT).append(ProductConstants.PARAM_VALUE_CHAR).append(creator.getUserName());
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
        if (Objects.isNull(productObj)) {
            Product product = new Product();
            product.setProductId(productLite.getProductId());
            product.setProductURL(url);
            product.setUniqueKey(ProductUtil.generateUniqueKey(url, true));
            return productRepository.save(product);
        }
        return productObj;
    }

    private boolean checkIfProductExists(String url, String creatorId) throws ProductException {
        log.debug("Check if Product {} already Exists", url);
        String shortURL = ProductUtil.generateUniqueKey(url, true);
        Product product = productRepository.findByUniqueKey(shortURL).orElse(null);
        if (Objects.nonNull(product)) {
            Optional<CreatorProduct> existingProduct = creatorProductRepository.findByProductId(product.getId(), creatorId);
            if (Objects.nonNull(existingProduct)) {
                log.info("Product Already Exists");
                return true;
            }
        }
        return false;
    }


    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAllProductsByCreator(String userName, int page, int limit) throws CreatorProductException {
        boolean hasNext;
        try {
            List<CreatorProductLite> products = new ArrayList<>();
            CreatorProfile creator = creatorRepository.findById(userName).orElse(null);
            if (Objects.nonNull(creator)) {
                log.info("Fetching products for {}", creator.getUserName());
                PageRequest pageRequest = PageRequest.of(page, limit);
                Page<CreatorProduct> creatorProducts = creatorProductRepository.findAllByCreatorId(creator.getUserName(), pageRequest);
                hasNext = (creatorProducts.getTotalPages() - 1) - page > 0;
                if (creatorProducts.isEmpty()) {
                    throw new CreatorProductException(ProductConstants.NO_VALID_PRODUCTS);
                }
                for (CreatorProduct p : creatorProducts) {
                    CreatorProductLite creatorProductDTO = new CreatorProductLite();
                    creatorProductDTO.setId(p.getId());
                    creatorProductDTO.setPid(p.getProduct().getProductId());
                    creatorProductDTO.setImageURL(p.getImageUrl());
                    creatorProductDTO.setAffiliateUrl(ProductUtil.getAffiliateUrl(p.getAffiliateCode()));
                    creatorProductDTO.setPrice(p.getPrice());
                    creatorProductDTO.setTitle(p.getTitle());
                    creatorProductDTO.setCreatedTime(p.getCreatedTime());
                    creatorProductDTO.setCurrency(p.getCurrency());
                    creatorProductDTO.setCategory(p.getCategory());
                    creatorProductDTO.setExpiryDate(p.getAffiliateExpiresAt());
                    products.add(creatorProductDTO);
                }
            } else {
                log.error(CreatorExceptionConstants.CREATOR_NOT_FOUND);
                throw new CreatorException(CreatorExceptionConstants.CREATOR_NOT_FOUND);
            }

            return enrichCreatorProductItems(products, page, hasNext);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CreatorProductException(ex.getMessage());
        }
    }

    private Map<String, Object> enrichCreatorProductItems(List<CreatorProductLite> products, int page, boolean hasNext) {
        return Map.of(
                DATA, products,
                PAGING, Map.of(
                        CURRENT_PAGE, page,
                        HAS_NEXT, hasNext
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CreatorProductLite getProductById(String productId) throws CreatorProductException, CreatorException {
        if (!StringUtils.hasLength(productId)) {
            throw new CreatorProductException(ProductConstants.NO_VALID_PRODUCTS);
        }
        CreatorProduct product = creatorProductRepository.findById(productId).orElse(null);
        if (Objects.nonNull(product)) {
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
        } else {
            log.error(ProductConstants.NO_VALID_PRODUCTS);
            throw new CreatorProductException(ProductConstants.NO_VALID_PRODUCTS);
        }
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
