package com.server.storefront.service;

import com.server.storefront.dto.CollectionLite;
import com.server.storefront.exception.CreatorCollectionException;

import java.util.Map;

public interface CreatorCollectionService {

    Map<String, Object> getAllCollectionsByCreator(String creator, int page, int limit) throws CreatorCollectionException;

    CollectionLite getCollectionById(String collectionId) throws CreatorCollectionException;

    boolean deleteCollectionById(String collectionId) throws CreatorCollectionException;

    CollectionLite updateCollectionById(String collectionId, CollectionLite collection) throws CreatorCollectionException;
}
