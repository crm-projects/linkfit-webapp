package com.server.storefront.utils.interceptors;

import com.server.storefront.Profile;
import com.server.storefront.utils.ApplicationConstants;
import com.server.storefront.utils.factory.UserProfileFactory;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;


/**
 * RESTValidationInterceptor acts as single entry point for all Incoming Http Requests.
 * It performs authentication and authorization.
 *
 * @author Vas
 * @version 1.0
 */
@Component
public class RESTValidationInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object object)
    {
        String user = request.getHeader(ApplicationConstants.STOREFRONT_USER);
        Profile userProfile = UserProfileFactory.generateUserProfile(user);
        if (Objects.nonNull(userProfile)) {
            request.setAttribute(ApplicationConstants.USER_PROFILE, userProfile);
        }
        return true;
    }

    @Override
    public void postHandle(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object object, ModelAndView model) {
    }

    @Override
    public void afterCompletion(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object object, Exception exception) {
    }
}
