package com.server.storefront.service;

import com.server.storefront.constants.CollectionConstants;
import com.server.storefront.constants.CreatorExceptionConstants;
import com.server.storefront.dto.CollectionLite;
import com.server.storefront.dto.CreatorProductLite;
import com.server.storefront.exception.CreatorCollectionException;
import com.server.storefront.exception.CreatorException;
import com.server.storefront.model.Collection;
import com.server.storefront.model.CreatorProduct;
import com.server.storefront.model.CreatorProfile;
import com.server.storefront.model.CollectionMedia;
import com.server.storefront.repository.CollectionRepository;
import com.server.storefront.repository.CreatorRepository;
import com.server.storefront.utils.ProductUtil;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatorCollectionServiceImpl implements CreatorCollectionService {

    private final CreatorRepository creatorRepository;

    private final CollectionRepository collectionRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAllCollectionsByCreator(String userName, int page, int limit) throws CreatorCollectionException {
        boolean hasNext;
        try {
            List<CollectionLite> collections = new ArrayList<>();
            CreatorProfile creator = creatorRepository.findByUserName(userName.strip()).orElseThrow(() -> new CreatorException(CreatorExceptionConstants.CREATOR_NOT_FOUND));

            if (Objects.nonNull(creator)) {
                log.info("Fetching collections for {}", userName);
                PageRequest pageRequest = PageRequest.of(page, limit);
                Optional<Page<Collection>> data = collectionRepository.findAllByCreatorId(creator.getId(), pageRequest);
                data.ifPresent(Slice::getContent);
                return new HashMap<>();
////                Page<Tuple> creatorCollections = collectionRepository.findAllByCreatorId(creator.getId(), pageRequest);
//                hasNext = (creatorCollections.getTotalPages() - 1) - page > 0;
//                if (creatorCollections.isEmpty())
//                    throw new CreatorCollectionException(CollectionConstants.NO_VALID_COLLECTIONS);
//
//                for (Tuple tuple : creatorCollections) {
//                    log.info("Scrubbing creator collections for Creator: {}", userName);
//                    Optional.ofNullable(scrubCreatorCollectionItems(tuple)).ifPresent(collections::add);
//                }
//                return enrichCreatorCollectionItems(collections, page, hasNext);
            } else {
                log.error(CreatorExceptionConstants.CREATOR_NOT_FOUND);
                throw new CreatorException(CreatorExceptionConstants.CREATOR_NOT_FOUND);
            }

        } catch (Exception ex) {
            throw new CreatorCollectionException(ex.getMessage());
        }
    }

    private CollectionLite scrubCreatorCollectionItems(Tuple tuple) {
        if (Boolean.TRUE.equals(tuple.get(CollectionConstants.ACTIVE_IND, Boolean.class))) {
            CollectionLite collectionDTO = new CollectionLite();
            collectionDTO.setId(tuple.get(CollectionConstants.ID, String.class));
            collectionDTO.setName(tuple.get(CollectionConstants.NAME, String.class));
            collectionDTO.setDescription(tuple.get(CollectionConstants.DESCRIPTION, String.class));
            collectionDTO.setImageURL(tuple.get(CollectionConstants.IMAGE_URL, String.class));

            String mId = tuple.get(CollectionConstants.MEDIA_ID, String.class);
            if (StringUtils.hasLength(mId)) {
                boolean isActive = tuple.get(CollectionConstants.MEDIA_ACTIVE_IND, Boolean.class);
                if (isActive) {
                    CollectionMedia collectionMedia = new CollectionMedia();
                    collectionMedia.setId(mId);
                    collectionMedia.setThumbNailURL(tuple.get(CollectionConstants.THUMBNAIL_URL, String.class));
                    collectionMedia.setMediaSource(tuple.get(CollectionConstants.MEDIA_SOURCE, String.class));
                    collectionMedia.setMediaId(tuple.get(CollectionConstants.SOURCE_MEDIA_ID, String.class));
                    collectionMedia.setActiveInd(tuple.get(CollectionConstants.MEDIA_ACTIVE_IND, Boolean.class));
                    collectionMedia.setMediaType(tuple.get(CollectionConstants.MEDIA_TYPE, String.class));
                    collectionDTO.setCollectionMedia(collectionMedia);
                }
            }
            return collectionDTO;
        }
        return null;
    }

    private Map<String, Object> enrichCreatorCollectionItems(List<CollectionLite> collections, int page, boolean hasNext) {
        Map<String, Object> pageObject = new HashMap<>();
        pageObject.put(CollectionConstants.CURRENT_PAGE, page);
        pageObject.put(CollectionConstants.HAS_NEXT_PAGE, hasNext);

        Map<String, Object> collectionList = new HashMap<>();
        collectionList.put(CollectionConstants.COLLECTIONS, collections);
        collectionList.put(CollectionConstants.PAGING, pageObject);

        return collectionList;
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionLite getCollectionById(String collectionId) throws CreatorCollectionException {
        try {
            if (!StringUtils.hasLength(collectionId))
                throw new CreatorCollectionException(CollectionConstants.NO_VALID_COLLECTIONS);

            Collection collection = collectionRepository.findById(collectionId).orElse(null);
            if (Objects.nonNull(collection)) {
                CollectionLite collectionDTO = new CollectionLite();
                collectionDTO.setId(collection.getId());
                collectionDTO.setName(collection.getName());
                collectionDTO.setDescription(collection.getDescription());
                collectionDTO.setImageURL(collection.getImageURL());
                collectionDTO.setCollectionMedia(collection.getCollectionMedia());

                Optional.ofNullable(collection.getCreatorProducts()).ifPresent(creatorProducts -> {
                    Set<CreatorProductLite> products = new HashSet<>();
                    for (CreatorProduct product : creatorProducts) {
                        CreatorProductLite creatorProductDTO = new CreatorProductLite();
                        creatorProductDTO.setId(product.getId());
                        creatorProductDTO.setTitle(product.getTitle());
                        creatorProductDTO.setImageURL(product.getImageUrl());
                        creatorProductDTO.setExpiryDate(product.getAffiliateExpiresAt());
                        creatorProductDTO.setPrice(product.getPrice());
                        creatorProductDTO.setCreatedTime(product.getCreatedTime());
                        creatorProductDTO.setAffiliateUrl(ProductUtil.getAffiliateUrl(product.getAffiliateCode()));
                        creatorProductDTO.setCreatedTime(product.getCreatedTime());
                        creatorProductDTO.setCurrency(product.getCurrency());
                        creatorProductDTO.setCategory(product.getCategory());
                        creatorProductDTO.setExpiryDate(product.getAffiliateExpiresAt());
                        products.add(creatorProductDTO);
                    }

                    collectionDTO.setProducts(products);
                });

                return collectionDTO;
            } else {
                log.error(CollectionConstants.NO_VALID_COLLECTIONS);
                throw new CreatorCollectionException(CollectionConstants.NO_VALID_COLLECTIONS);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CreatorCollectionException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean deleteCollectionById(String collectionId) throws CreatorCollectionException {
        if (!StringUtils.hasLength(collectionId)) {
            throw new CreatorCollectionException(CollectionConstants.NO_VALID_COLLECTIONS);
        }

        Collection collection = collectionRepository.findById(collectionId).orElseThrow(() -> new CreatorCollectionException(CollectionConstants.NO_VALID_COLLECTIONS));
        collection.setActiveInd(false);
        collectionRepository.save(collection);
        return true;
    }

    @Override
    @Transactional
    public CollectionLite updateCollectionById(String collectionId, CollectionLite collection) throws CreatorCollectionException {
        if (!StringUtils.hasLength(collectionId)) {
            throw new CreatorCollectionException(CollectionConstants.NO_VALID_COLLECTIONS);
        }

        if(Objects.isNull(collection)) throw new CreatorCollectionException("");

        Collection existingCollection = collectionRepository.findById(collectionId).orElseThrow(() -> new CreatorCollectionException(""));
        existingCollection.setName(collection.getName());
        existingCollection.setDescription(collection.getDescription());
        existingCollection.setImageURL(collection.getImageURL());
        existingCollection.setCollectionMedia(collection.getCollectionMedia());
        existingCollection.setCreatorId(collection.getCreatorId());
        Optional.ofNullable(collection.getProducts()).ifPresent(product -> {
            Set<CreatorProduct> products = existingCollection.getCreatorProducts();
//            for (CreatorProductDTO productDto : product) {
//                CreatorProductDTO creatorProductDTO = new CreatorProductDTO();
//                creatorProductDTO.setId(product.getId());
//                creatorProductDTO.setTitle(product.getTitle());
//                creatorProductDTO.setImageURL(product.getImageUrl());
//                creatorProductDTO.setExpiryDate(product.getAffiliateExpiresAt());
//                creatorProductDTO.setPrice(product.getPrice());
//                creatorProductDTO.setCreatedTime(product.getCreatedTime());
//                creatorProductDTO.setAffiliateUrl(ProductUtil.getAffiliateUrl(product.getAffiliateCode()));
//                creatorProductDTO.setCreatedTime(product.getCreatedTime());
//                creatorProductDTO.setCurrency(product.getCurrency());
//                creatorProductDTO.setCategory(product.getCategory());
//                creatorProductDTO.setExpiryDate(product.getAffiliateExpiresAt());
//                products.add(creatorProductDTO);
//            }


//            collectionDTO.setProducts(products);
        });


        /**
         * To add update logic based on input payload
         */
        return null;
    }
}
