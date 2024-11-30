package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductNode {

    @JsonProperty("urls")
    private List<String> products;

    @JsonProperty("collection_ind")
    private boolean collectionInd;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("collection")
    private CollectionLite collection;

}
