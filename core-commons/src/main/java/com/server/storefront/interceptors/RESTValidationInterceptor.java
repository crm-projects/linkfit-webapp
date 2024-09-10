package com.server.storefront.interceptors;

import com.server.storefront.model.Profile;
import com.server.storefront.constants.ApplicationConstants;
import com.server.storefront.Util;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
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

    private static final String BASE_PATH = "/v1";

    @Override
    public boolean preHandle(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object object) {
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
