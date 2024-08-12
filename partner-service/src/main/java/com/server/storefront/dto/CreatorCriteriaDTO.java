package com.server.storefront.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatorCriteriaDTO {

    private boolean portfolioRequired;

    private int minFollowers;

    private List<String> categories;
}
