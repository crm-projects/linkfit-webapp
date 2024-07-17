package com.server.storefront.service;

import com.server.storefront.model.creator.Creator;
import com.server.storefront.model.creator.CreatorLite;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.utils.exception.RandomGeneratorException;
import com.server.storefront.model.auth.SignIn;
import com.server.storefront.model.auth.SignUp;

public interface CreatorService {

    SignUp creatorSignUp(SignUp authObj) throws CreatorException, RandomGeneratorException;

    Creator creatorSignIn(SignIn authObj);

    Creator saveCreatorProfile(CreatorLite creatorLite) throws CreatorException;

    Creator deleteCreatorProfile(CreatorLite creatorLite);
}
