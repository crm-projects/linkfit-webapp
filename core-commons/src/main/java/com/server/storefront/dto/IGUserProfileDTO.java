package com.server.storefront.dto;

import lombok.Data;

@Data
public class IGUserProfileDTO {

    private String userName;
    private long followersCount;
    private boolean isConnected;
}
