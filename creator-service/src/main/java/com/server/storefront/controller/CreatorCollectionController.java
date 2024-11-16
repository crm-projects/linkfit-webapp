package com.server.storefront.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.server.storefront.dto.CollectionDTO;
import com.server.storefront.exception.CreatorCollectionException;
import com.server.storefront.exception.CreatorProductException;
import com.server.storefront.helper.Views;
import com.server.storefront.service.CollectionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CreatorCollectionController {

    private final CollectionService collectionService;

    @JsonView(Views.Private.class)
    @GetMapping("/{user_name}/collections")
    public ResponseEntity<Map<String, Object>> getAllCollectionsByCreator(@PathVariable("user_name") String creatorId,
                                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                                          @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                                          HttpServletRequest request) throws CreatorCollectionException {
        try {
            return new ResponseEntity<>(collectionService.getAllCollectionsByCreator(creatorId, page, limit), HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CreatorCollectionException(ex.getMessage());
        }
    }

    @JsonView(Views.Private.class)
    @GetMapping("/collections/{collection_id}")
    public ResponseEntity<CollectionDTO> getCollectionById(@PathVariable("collection_id") String collectionId, HttpServletRequest request)
            throws CreatorCollectionException {
        try {
            return new ResponseEntity<>(collectionService.getCollectionById(collectionId), HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CreatorCollectionException(ex.getMessage());
        }
    }

    @DeleteMapping(value = "/collections/{collection_id}")
    public ResponseEntity<Boolean> deleteCreatorCollectionById(@PathVariable(value = "collection_id") String collectionId, HttpServletRequest request) throws CreatorProductException {
        try {
            return new ResponseEntity<>(collectionService.deleteCollectionById(collectionId), HttpStatus.OK);
        } catch (Exception ex) {
            throw new CreatorProductException(ex.getMessage());
        }
    }

    @PutMapping(value = "/collections/{collection_id}")
    public ResponseEntity<CollectionDTO> updateCreatorCollectionById(@PathVariable(value = "collection_id") String collectionId, @RequestBody CollectionDTO collection,
                                                                     HttpServletRequest request) throws CreatorProductException {
        try {
            return new ResponseEntity<>(collectionService.updateCollectionById(collectionId, collection), HttpStatus.OK);
        } catch (Exception ex) {
            throw new CreatorProductException(ex.getMessage());
        }
    }

}
