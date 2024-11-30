package com.server.storefront.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CriteriaLite {

    private boolean portfolioRequired;

    private int minFollowers;

    private List<String> categories;
}
