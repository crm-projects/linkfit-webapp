package com.server.storefront.service;

import com.server.storefront.dto.CollectionDTO;
import com.server.storefront.exception.CreatorCollectionException;

import java.util.Map;

public interface CollectionService {

    Map<String, Object> getAllCollectionsByCreator(String creator, int page, int limit) throws CreatorCollectionException;

    CollectionDTO getCollectionById(String collectionId) throws CreatorCollectionException;
}
