package com.server.storefront.service;

import com.server.storefront.model.Creator;
import com.server.storefront.model.CreatorLite;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.utils.holder.SignUp;

public interface CreatorService {

    Creator saveUpdateCreatorProfileSettings(CreatorLite creatorLite) throws CreatorException;

    SignUp authenticateCreator(SignUp signUp) throws CreatorException;
}
