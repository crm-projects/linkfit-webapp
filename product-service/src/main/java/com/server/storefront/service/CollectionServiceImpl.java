package com.server.storefront.service;

import com.server.storefront.constants.CollectionConstants;
import com.server.storefront.constants.ExceptionConstants;
import com.server.storefront.dto.CollectionDTO;
import com.server.storefront.dto.CreatorProductDTO;
import com.server.storefront.exception.CreatorCollectionException;
import com.server.storefront.exception.CreatorException;

import com.server.storefront.model.Collection;
import com.server.storefront.model.CreatorProduct;
import com.server.storefront.model.CreatorProfile;
import com.server.storefront.model.MediaData;
import com.server.storefront.repository.CollectionRepository;
import com.server.storefront.repository.CreatorRepository;

import com.server.storefront.utils.ProductUtil;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CreatorRepository creatorRepository;

    private final CollectionRepository collectionRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAllCollectionsByCreator(String userName, int page, int limit) throws CreatorCollectionException {
        boolean hasNext;
        try {
            List<CollectionDTO> collections = new ArrayList<>();
            CreatorProfile creator = creatorRepository.findByUserName(userName.strip()).orElse(null);
            if (Objects.nonNull(creator)) {
                log.info("Fetching collections for {}", userName);
                PageRequest pageRequest = PageRequest.of(page, limit);
                Page<Tuple> creatorCollections = collectionRepository.findAllByCreatorId(creator.getId(), pageRequest);
                hasNext = (creatorCollections.getTotalPages() - 1) - page > 0;
                if (creatorCollections.isEmpty())
                    throw new CreatorCollectionException(CollectionConstants.NO_VALID_COLLECTIONS);

                for (Tuple tuple : creatorCollections) {
                    log.info("Scrubbing creator collections for Creator: {}", userName);
                    Optional.ofNullable(scrubCreatorCollectionItems(tuple)).ifPresent(collections::add);
                }
                return enrichCreatorCollectionItems(collections, page, hasNext);
            } else {
                log.error(ExceptionConstants.CREATOR_NOT_FOUND);
                throw new CreatorException(ExceptionConstants.CREATOR_NOT_FOUND);
            }

        } catch (Exception ex) {
            throw new CreatorCollectionException(ex.getMessage());
        }
    }

    private CollectionDTO scrubCreatorCollectionItems(Tuple tuple) {
        if (Boolean.TRUE.equals(tuple.get(CollectionConstants.ACTIVE_IND, Boolean.class))) {
            CollectionDTO collectionDTO = new CollectionDTO();
            collectionDTO.setId(tuple.get(CollectionConstants.ID, String.class));
            collectionDTO.setName(tuple.get(CollectionConstants.NAME, String.class));
            collectionDTO.setDescription(tuple.get(CollectionConstants.DESCRIPTION, String.class));
            collectionDTO.setImageURL(tuple.get(CollectionConstants.IMAGE_URL, String.class));

            String mId = tuple.get(CollectionConstants.MEDIA_ID, String.class);
            if (StringUtils.hasLength(mId)) {
                boolean isActive = tuple.get(CollectionConstants.MEDIA_ACTIVE_IND, Boolean.class);
                if (isActive) {
                    MediaData mediaData = new MediaData();
                    mediaData.setId(mId);
                    mediaData.setThumbNailURL(tuple.get(CollectionConstants.THUMBNAIL_URL, String.class));
                    mediaData.setMediaSource(tuple.get(CollectionConstants.MEDIA_SOURCE, String.class));
                    mediaData.setMediaId(tuple.get(CollectionConstants.SOURCE_MEDIA_ID, String.class));
                    mediaData.setActiveInd(tuple.get(CollectionConstants.MEDIA_ACTIVE_IND, Boolean.class));
                    mediaData.setMediaType(tuple.get(CollectionConstants.MEDIA_TYPE, String.class));
                    collectionDTO.setMediaData(mediaData);
                }
            }
            return collectionDTO;
        }
        return null;
    }

    private Map<String, Object> enrichCreatorCollectionItems(List<CollectionDTO> collections, int page, boolean hasNext) {
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
    public CollectionDTO getCollectionById(String collectionId) throws CreatorCollectionException {
        try {
            if (!StringUtils.hasLength(collectionId))
                throw new CreatorCollectionException(CollectionConstants.NO_VALID_COLLECTIONS);

            Collection collection = collectionRepository.findById(collectionId).orElse(null);
            if (Objects.nonNull(collection)) {
                CollectionDTO collectionDTO = new CollectionDTO();
                collectionDTO.setId(collection.getId());
                collectionDTO.setName(collection.getName());
                collectionDTO.setDescription(collection.getDescription());
                collectionDTO.setImageURL(collection.getImageURL());
                collectionDTO.setMediaData(collection.getMediaData());

                Optional.ofNullable(collection.getCreatorProducts()).ifPresent(creatorProducts -> {
                    Set<CreatorProductDTO> products = new HashSet<>();
                    for (CreatorProduct product : creatorProducts) {
                        CreatorProductDTO creatorProductDTO = new CreatorProductDTO();
                        creatorProductDTO.setId(product.getId());
                        creatorProductDTO.setTitle(product.getTitle());
                        creatorProductDTO.setImageURL(product.getImageURL());
                        creatorProductDTO.setExpiryDate(product.getAffiliateExpiresAt());
                        creatorProductDTO.setPrice(product.getPrice());
                        creatorProductDTO.setCreatedTime(product.getCreatedTime());
                        creatorProductDTO.setAffiliateUrl(ProductUtil.getAffiliateUrl(product.getAffiliateCode()));
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
}
