package com.server.storefront.service;


import com.server.storefront.model.admin.Plan;
import com.server.storefront.model.admin.Platform;

public interface AdminService {

    boolean savePlanDetails(Plan plan);

    Platform savePlatformItems(Platform platform);
}
