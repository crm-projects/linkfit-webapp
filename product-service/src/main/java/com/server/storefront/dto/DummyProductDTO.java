package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DummyProductDTO {

    @JsonProperty("urls")
    private List<String> products;

    @JsonProperty("collection_ind")
    private boolean collectionInd;

    @JsonProperty("collection")
    private CollectionDTO collection;

}
