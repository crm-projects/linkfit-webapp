package com.server.storefront.service;

import com.server.storefront.model.Creator;
import com.server.storefront.model.CreatorLite;

public interface CreatorService {


    boolean saveUpdateCreatorDetails(CreatorLite creatorLite);

    Creator saveUpdatePlatformDetails(CreatorLite creatorLite);
}
