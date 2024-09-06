package com.server.storefront.service;

import com.server.storefront.constants.ApplicationConstants;
import com.server.storefront.constants.ExceptionConstants;
import com.server.storefront.constants.ProductConstants;
import com.server.storefront.dto.CreatorProductDTO;
import com.server.storefront.dto.ProductDTO;
import com.server.storefront.exception.*;
import com.server.storefront.factory.PartnerHandlerFactory;
import com.server.storefront.handler.BaseHandler;
import com.server.storefront.model.CreatorProduct;
import com.server.storefront.model.CreatorProfile;
import com.server.storefront.model.Product;
import com.server.storefront.repository.CreatorProductRepository;
import com.server.storefront.repository.CreatorRepository;
import com.server.storefront.repository.PartnerRepository;
import com.server.storefront.repository.ProductRepository;
import com.server.storefront.utils.PartnerUtil;
import com.server.storefront.utils.ProductUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final PartnerHandlerFactory productHandlerFactory;

    private final PartnerRepository partnerRepository;

    private final CreatorRepository creatorRepository;

    private final CreatorProductRepository creatorProductRepository;

    private final ProductRepository productRepository;


    @Override
    @Transactional
    public ProductDTO addProduct(String url, String creatorId) throws ProductException {
        log.info("Adding product {}", url);
        try {
            return checkProfileAndValidateProduct(url, creatorId);
        } catch (Exception e) {
            throw new ProductException(e.getMessage());
        }
    }

    private ProductDTO checkProfileAndValidateProduct(String url, String creatorId) throws HandlerException, ProductException,
            CreatorException, CreatorProductException, PartnerException {
        CreatorProfile creatorObj = creatorRepository.findById(creatorId).orElse(null);
        if (Objects.nonNull(creatorObj)) {
            if (!checkIfProductExists(url, creatorId)) {
                ProductDTO productDTO = validateHandlerAndFetchProduct(url);
                Product productObj = validateProductAndSaveIfAbsent(productDTO, url);
                return scrubAndSaveCreatorProduct(productDTO, productObj, creatorId, url, creatorObj);
            } else throw new CreatorProductException("Product Already Exists");
        } else throw new CreatorException(ExceptionConstants.CREATOR_NOT_FOUND);
    }

    private ProductDTO validateHandlerAndFetchProduct(String url) throws HandlerException, ProductException, PartnerException {
        String partner = PartnerUtil.getDomainName(url);
        if (partnerRepository.existsByName(partner)) {
            BaseHandler handler = productHandlerFactory.getHandler(partner);
            return retrieveProductDetailsFromPartner(handler, url, partner);
        } else {
            log.info(ProductConstants.NO_VALID_PARTNER);
            return new ProductDTO();
        }
    }

    private ProductDTO retrieveProductDetailsFromPartner(BaseHandler handler, String url, String partner) throws ProductException, PartnerException {
        log.debug("Retrieve Product Details for {} from Partner - {}", url, partner);
        ResponseEntity<ProductDTO> productDetails = handler.getProductDetails(url);
        if (Objects.nonNull(productDetails) && (productDetails.getBody() != null)) {
            productDetails.getBody().setPartnerName(partner);
            return productDetails.getBody();
        } else throw new ProductException(ExceptionConstants.PRODUCT_DETAILS_NOT_FOUND);
    }

    private ProductDTO scrubAndSaveCreatorProduct(ProductDTO productDTO, Product productObj, String creatorId, String url, CreatorProfile creatorObj) throws ProductException {
        Date now = new Date();
        CreatorProduct creatorProduct = new CreatorProduct();
        creatorProduct.setActiveInd(true);
        creatorProduct.setCreatedBy(creatorId);
        creatorProduct.setCreatedTime(now);
        creatorProduct.setCreator(creatorObj);
        creatorProduct.setProduct(productObj);
        creatorProduct.setImageURL(productDTO.getImageUrl());
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
        return productDTO;

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
                    productDTO.setImageURL(p.getImageURL());
                    productDTO.setAffiliateCode(getAffiliateUrl(p.getAffiliateCode()));
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


    private String getAffiliateUrl(String code) {
        return ProductConstants.AFFILIATE_BASE_URL + ApplicationConstants.PARAM_SEPARATOR_CHAR + code;
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
            creatorProductDTO.setAffiliateCode(product.getAffiliateCode());
            creatorProductDTO.setTitle(product.getTitle());
            creatorProductDTO.setImageURL(product.getImageURL());
            return creatorProductDTO;
        } else {
            log.error(ProductConstants.NO_VALID_PRODUCTS);
            throw new CreatorProductException(ProductConstants.NO_VALID_PRODUCTS);
        }
    }
}
