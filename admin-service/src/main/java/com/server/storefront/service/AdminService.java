package com.server.storefront.service;


import com.server.storefront.dto.PartnerDTO;
import com.server.storefront.model.Partner;
import com.server.storefront.model.Platform;

public interface AdminService {
    Platform savePlatformItems(Platform platform);

    Partner savePartnerItems(PartnerDTO partnerDTO);
}
