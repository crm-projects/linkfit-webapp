package com.server.storefront.service;

import com.server.storefront.constants.CollectionConstants;
import com.server.storefront.constants.CreatorExceptionConstants;
import com.server.storefront.dto.CollectionLite;
import com.server.storefront.dto.CreatorProductLite;
import com.server.storefront.exception.CreatorCollectionException;
import com.server.storefront.exception.CreatorException;
import com.server.storefront.exception.CreatorProductException;
import com.server.storefront.model.Collection;
import com.server.storefront.model.CreatorProduct;
import com.server.storefront.model.CreatorProfile;
import com.server.storefront.model.CollectionMedia;
import com.server.storefront.repository.CollectionRepository;
import com.server.storefront.repository.CreatorRepository;
import com.server.storefront.utils.ProductUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Map<String, Object> getAllCollectionsByCreator(String userName, int startIndex, int limit) throws CreatorCollectionException {
        try {
            List<CollectionLite> collections = new ArrayList<>();
            CreatorProfile creator = creatorRepository.findByUserName(userName.strip())
                    .orElseThrow(() -> new CreatorException(CreatorExceptionConstants.CREATOR_NOT_FOUND));

            log.info("Fetching collections for {}", userName);

            /* TODO : Cache this result as product count won't increase at once */
            int totalCount = collectionRepository.fetchCount(creator.getId());
            
            List<Collection> collectionList = collectionRepository.findAllByCreatorId(creator.getId(), startIndex, limit);
            boolean hasNext = startIndex + collectionList.size() < totalCount;

            if (collectionList.isEmpty()) {
                throw new CreatorProductException(CollectionConstants.NO_VALID_COLLECTIONS);
            }
            
            collectionList.forEach(collection -> {
                CollectionLite collectionDTO = new CollectionLite();
                collectionDTO.setId(collection.getId());
                collectionDTO.setName(collection.getName());
                collectionDTO.setDescription(collection.getDescription());
                collectionDTO.setImageURL(collection.getImageURL());

                if (collection.getCollectionMedia() != null) {
                    CollectionMedia collectionMedia = new CollectionMedia();
                    collectionMedia.setId(collection.getCollectionMedia().getId());
                    collectionMedia.setThumbNailURL(collection.getCollectionMedia().getThumbNailURL());
                    collectionMedia.setMediaSource(collection.getCollectionMedia().getMediaSource());
                    collectionMedia.setMediaId(collection.getCollectionMedia().getMediaId());
                    collectionMedia.setActiveInd(collection.getCollectionMedia().isActiveInd());
                    collectionMedia.setMediaType(collection.getCollectionMedia().getMediaType());
                    collectionDTO.setCollectionMedia(collectionMedia);
                }

                collections.add(collectionDTO);
            });

            return enrichCreatorCollectionItems(collections, totalCount, hasNext);
        } catch (Exception ex) {
            throw new CreatorCollectionException(ex.getMessage());
        }
    }

    private Map<String, Object> enrichCreatorCollectionItems(List<CollectionLite> collections, int totalCount, boolean hasNext) {
        return Map.of(
                CollectionConstants.COLLECTIONS, collections,
                CollectionConstants.PAGING, Map.of(
                        CollectionConstants.TOTAL_COUNT, totalCount,
                        CollectionConstants.HAS_NEXT_PAGE, hasNext
                )
        );
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

        if (collection != null) throw new CreatorCollectionException(CollectionConstants.NO_VALID_COLLECTIONS);

        Collection existingCollection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new CreatorCollectionException(CollectionConstants.NO_VALID_COLLECTIONS));

        existingCollection.setName(collection.getName());
        existingCollection.setDescription(collection.getDescription());
        existingCollection.setImageURL(collection.getImageURL());
        existingCollection.setCollectionMedia(collection.getCollectionMedia());
        existingCollection.setCreatorId(collection.getCreatorId());

        collectionRepository.save(existingCollection);
        return collection;
    }
}
