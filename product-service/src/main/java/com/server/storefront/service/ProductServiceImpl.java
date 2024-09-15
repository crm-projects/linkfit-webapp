package com.server.storefront.service;

import com.server.storefront.constants.AffiliateConstants;
import com.server.storefront.constants.ApplicationConstants;
import com.server.storefront.constants.ExceptionConstants;
import com.server.storefront.constants.ProductConstants;
import com.server.storefront.dto.CollectionDTO;
import com.server.storefront.dto.CreatorProductDTO;
import com.server.storefront.dto.DummyProductDTO;
import com.server.storefront.dto.ProductDTO;
import com.server.storefront.exception.*;
import com.server.storefront.factory.PartnerHandlerFactory;
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
public class ProductServiceImpl implements ProductService {

    private final PartnerHandlerFactory productHandlerFactory;

    private final PartnerRepository partnerRepository;

    private final CreatorRepository creatorRepository;

    private final CreatorProductRepository creatorProductRepository;

    private final ProductRepository productRepository;

    private final CollectionRepository collectionRepository;

    private final MediaRepository mediaRepository;

    /**
     * TODO - Should Re-write {@link DummyProductDTO}
     */
    @Override
    @Transactional
    public Map<String, Object> addProduct(DummyProductDTO dto, String userName) throws ProductException {
        log.info("Adding products for {}", userName);
        try {
            return checkProfileAndValidateProduct(dto, userName);
        } catch (Exception e) {
            throw new ProductException(e.getMessage());
        }
    }

    private Map<String, Object> checkProfileAndValidateProduct(DummyProductDTO dto, String userName) throws CreatorException, InterruptedException {
        CreatorProfile creator = creatorRepository.findByUserName(userName.strip()).orElseThrow(() -> new CreatorException(ExceptionConstants.CREATOR_NOT_FOUND));

        Map<String, Object> resp = new HashMap<>();
        if (Objects.nonNull(dto)) {
            CompletableFuture<Set<CreatorProduct>> validateProductAndSaveIfAbsent = CompletableFuture.supplyAsync(() -> processProductItems(dto.getProducts(), userName, creator));
            CompletableFuture<Collection> validateCollection = CompletableFuture.supplyAsync(() -> {
                if (dto.isCollectionInd()) return scrubCollectionItems(dto.getCollection());
                return null;
            });
            CompletableFuture<Map<String, Object>> response = validateProductAndSaveIfAbsent.thenCombine(validateCollection, this::preparePostProcessing);

            try {
                resp = response.get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new InterruptedException("Error while processing creator products and collection");
            }
        }
        return resp;
    }

    private Map<String, Object> preparePostProcessing(Set<CreatorProduct> products, Collection collection) {
        if (Objects.nonNull(collection)) {
            collection.setCreatorProducts(products);
            collectionRepository.save(collection);
        }

        List<ProductDTO> productList = new ArrayList<>();
        for (CreatorProduct p : products) {
            ProductDTO productDTO = new ProductDTO();
            /**
             * To Add Data Mapping Logic
             */
            productList.add(productDTO);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("products", productList);
        result.put("collection", collection);
        return result;
    }

    private Collection scrubCollectionItems(CollectionDTO collectionDTO) {
        Collection collectionObj = new Collection();
        Optional.ofNullable(collectionDTO).ifPresent((collection -> {
            collectionObj.setName(collection.getName());
            collectionObj.setDescription(collection.getDescription());
            collectionObj.setImageURL(collection.getDescription());
            collectionObj.setActiveInd(true);

            if (collection.isMediaInd()) {

                MediaData mediaData = collection.getMediaData();
                if (!StringUtils.hasText(mediaData.getId())) {
                    mediaData.setId(UUID.randomUUID().toString());
                    mediaData = mediaRepository.save(mediaData);
                }
                collectionObj.setMediaData(mediaData);
            }
        }));
        return collectionObj;
    }

    private Set<CreatorProduct> processProductItems(List<String> products, String userName, CreatorProfile creator) {
        if (CollectionUtils.isEmpty(products)) return Collections.emptySet();
        Set<CreatorProduct> productSet = new HashSet<>();
        for (String url : products) {
            try {
                CreatorProduct creatorProduct = sanitizeAndProcessCreatorItems(url, userName, creator);
                productSet.add(creatorProduct);
            } catch (ProductException | HandlerException | PartnerException | CreatorProductException e) {
                log.error("Error while processing creator product", e);
            }
        }
        return productSet;
    }

    private CreatorProduct sanitizeAndProcessCreatorItems(String url, String creatorId, CreatorProfile creator) throws ProductException, HandlerException, PartnerException, CreatorProductException {
        if (!checkIfProductExists(url, creatorId)) {
            ProductDTO productDTO = validateHandlerAndFetchProduct(url);
            Product productObj = validateProductAndSaveIfAbsent(productDTO, url);
            return scrubAndSaveCreatorProduct(productDTO, productObj, creatorId, url, creator);
        } else throw new CreatorProductException("Product Already Exists");
    }

    private ProductDTO validateHandlerAndFetchProduct(String url) throws HandlerException, ProductException, PartnerException {
        Map<String, Object> handlerParams = getPartnerHandler(url);
        if (!handlerParams.isEmpty()) {
            BaseHandler handler = (BaseHandler) handlerParams.get("handler");
            String partner = (String) handlerParams.get("partner");
            return retrieveProductDetailsFromPartner(handler, url, partner);
        }
        return new ProductDTO();
    }

    private Map<String, Object> getPartnerHandler(String url) throws HandlerException {
        String partner = PartnerUtil.getDomainName(url);
        if (partnerRepository.existsByName(partner)) {
            BaseHandler handler = productHandlerFactory.getHandler(partner);
            Map<String, Object> map = new HashMap<>();
            map.put("partner", partner);
            map.put("handler", handler);
            return map;
        } else {
            log.info(ProductConstants.NO_VALID_PARTNER);
        }
        return Collections.emptyMap();
    }

    private ProductDTO retrieveProductDetailsFromPartner(BaseHandler handler, String url, String partner) throws ProductException, PartnerException {
        log.debug("Retrieve Product Details for {} from Partner - {}", url, partner);
        ResponseEntity<ProductDTO> productDetails = handler.getProductDetails(url);
        if (Objects.nonNull(productDetails) && (productDetails.getBody() != null)) {
            productDetails.getBody().setPartnerName(partner);
            return productDetails.getBody();
        } else throw new ProductException(ExceptionConstants.PRODUCT_DETAILS_NOT_FOUND);
    }

    private CreatorProduct scrubAndSaveCreatorProduct(ProductDTO productDTO, Product productObj, String creatorId, String url, CreatorProfile creatorObj) throws ProductException {
        Date now = new Date();
        CreatorProduct creatorProduct = new CreatorProduct();
        creatorProduct.setActiveInd(true);
        creatorProduct.setCreatedBy(creatorId);
        creatorProduct.setCreatedTime(now);
        creatorProduct.setCreator(creatorObj);
        creatorProduct.setProduct(productObj);
        creatorProduct.setImageUrl(productDTO.getImageUrl());
        creatorProduct.setPrice(productDTO.getPrice());

        StringBuilder toHash = new StringBuilder(url);
        toHash.append(ApplicationConstants.PARAM_SEPARATOR_CHAR).append(ProductConstants.SALT).append(ApplicationConstants.PARAM_VALUE_CHAR).append(creatorId);
        String affiliateCode = ProductUtil.generateUniqueKey(toHash.toString(), false);
        if (StringUtils.hasLength(affiliateCode)) {
            productDTO.setAffiliateCode(affiliateCode);
            setAffiliateDetails(affiliateCode, creatorProduct, now);
        }

        creatorProductRepository.save(creatorProduct);
        log.info("Product Successfully Added for Creator {}", creatorObj.getUserName());
        return creatorProduct;
    }

    private void setAffiliateDetails(String code, CreatorProduct creatorProduct, Date now) {
        creatorProduct.setAffiliateCode(code);
        creatorProduct.setAffiliateExpiresAt(ProductUtil.validUntil(now));
    }

    Product validateProductAndSaveIfAbsent(ProductDTO productDTO, String url) throws ProductException {
        Product productObj = productRepository.findByProductId(productDTO.getProductId()).orElse(null);
        if (Objects.isNull(productObj)) {
            Product product = new Product();
            product.setProductId(productDTO.getProductId());
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
            CreatorProduct existingProduct = creatorProductRepository.findByProductId(product.getId(), creatorId).orElse(null);
            if (Objects.nonNull(existingProduct)) {
                log.error("Product Already Exists");
                return true;
            }
        }
        return false;
    }


    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAllProductsByCreator(String creatorId, int page, int limit) throws CreatorProductException {
        boolean hasNext;
        try {
            List<CreatorProductDTO> products = new ArrayList<>();
            CreatorProfile creator = creatorRepository.findById(creatorId).orElse(null);
            if (Objects.nonNull(creator)) {
                log.info("Fetching products for {}", creator.getUserName());
                PageRequest pageRequest = PageRequest.of(page, limit);
                Page<CreatorProduct> creatorProducts = creatorProductRepository.findAllByCreatorId(creatorId, pageRequest);
                hasNext = (creatorProducts.getTotalPages() - 1) - page > 0;
                if (creatorProducts.isEmpty()) {
                    throw new CreatorProductException(ProductConstants.NO_VALID_PRODUCTS);
                }
                for (CreatorProduct p : creatorProducts) {
                    CreatorProductDTO productDTO = new CreatorProductDTO();
                    productDTO.setId(p.getId());
                    productDTO.setPid(p.getProduct().getProductId());
                    productDTO.setImageURL(p.getImageUrl());
                    productDTO.setAffiliateUrl(ProductUtil.getAffiliateUrl(p.getAffiliateCode()));
                    productDTO.setPrice(p.getPrice());
                    productDTO.setTitle(p.getTitle());
                    products.add(productDTO);
                }
            } else {
                log.error(ExceptionConstants.CREATOR_NOT_FOUND);
                throw new CreatorException(ExceptionConstants.CREATOR_NOT_FOUND);
            }

            return enrichCreatorProductItems(products, page, hasNext);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CreatorProductException(ex.getMessage());
        }
    }

    private Map<String, Object> enrichCreatorProductItems(List<CreatorProductDTO> products, int page, boolean hasNext) {
        Map<String, Object> pageObject = new HashMap<>();
        pageObject.put("current_page", page);
        pageObject.put("has_next", hasNext);

        Map<String, Object> productList = new HashMap<>();
        productList.put("data", products);
        productList.put("paging", pageObject);

        return productList;
    }

    @Override
    @Transactional(readOnly = true)
    public CreatorProductDTO getProductById(String productId) throws CreatorProductException, CreatorException {
        if (!StringUtils.hasLength(productId)) {
            throw new CreatorProductException(ProductConstants.NO_VALID_PRODUCTS);
        }
        CreatorProduct product = creatorProductRepository.findById(productId).orElse(null);
        if (Objects.nonNull(product)) {
            CreatorProductDTO creatorProductDTO = new CreatorProductDTO();
            creatorProductDTO.setId(product.getId());
            creatorProductDTO.setPid(product.getProduct().getProductId());
            creatorProductDTO.setPrice(product.getPrice());
            creatorProductDTO.setAffiliateUrl(product.getAffiliateCode());
            creatorProductDTO.setTitle(product.getTitle());
            creatorProductDTO.setImageURL(product.getImageUrl());
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
                String redirectUrl = processRedirectUrl(baseProductUrl);
                return new RedirectView(redirectUrl);
            }
        }
        return null;
    }

    private String processRedirectUrl(String baseProductUrl) throws HandlerException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseProductUrl)
                .queryParam(AffiliateConstants.UTM_MEDIUM, AffiliateConstants.SOURCE)
                .queryParam(AffiliateConstants.UTM_SOURCE, AffiliateConstants.SOURCE)
                .queryParam(AffiliateConstants.PLATFORM_ID, AffiliateConstants.SOURCE);

        Map<String, Object> objectMap = getPartnerHandler(baseProductUrl);
        BaseHandler handler = (BaseHandler) objectMap.get("handler");
        return handler.buildRedirectUrl(builder);
    }
}
