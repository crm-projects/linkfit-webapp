package com.server.storefront.service;

import com.server.storefront.creator.Creator;
import com.server.storefront.creator.CreatorLite;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.utils.holder.SignUp;

public interface CreatorService {

    Creator saveUpdateCreatorProfileSettings(CreatorLite creatorLite, boolean isExistingCreator) throws CreatorException;

    SignUp authenticateCreator(SignUp authObj) throws CreatorException;
}
