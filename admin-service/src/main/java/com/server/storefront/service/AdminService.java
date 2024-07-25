package com.server.storefront.service;


import com.server.storefront.admin.model.Plan;
import com.server.storefront.admin.model.Platform;

public interface AdminService {

    boolean savePlanDetails(Plan plan);

    Platform savePlatformItems(Platform platform);
}
