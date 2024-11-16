package com.server.storefront.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IGProfile {

    private String userName;

    private long followerCount;

    private boolean isConnected;
}