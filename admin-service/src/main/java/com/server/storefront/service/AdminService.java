package com.server.storefront.service;


import com.server.storefront.admin.Plan;
import com.server.storefront.admin.Platform;

public interface AdminService {

    boolean savePlanDetails(Plan plan);

    Platform savePlatformItems(Platform platform);
}
