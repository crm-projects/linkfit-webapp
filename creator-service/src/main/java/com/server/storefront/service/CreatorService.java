package com.server.storefront.service;

import com.server.storefront.creator.model.Creator;
import com.server.storefront.creator.model.CreatorLite;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.utils.exception.RandomGeneratorException;
import com.server.storefront.auth.SignIn;
import com.server.storefront.auth.SignUp;

public interface CreatorService {

    SignUp creatorSignUp(SignUp authObj) throws CreatorException, RandomGeneratorException;

    Creator creatorSignIn(SignIn authObj);

    Creator saveCreatorProfile(CreatorLite creatorLite) throws CreatorException;

    Creator deleteCreatorProfile(CreatorLite creatorLite);
}
