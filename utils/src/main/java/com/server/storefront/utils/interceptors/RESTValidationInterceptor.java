package com.server.storefront.utils.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * RESTValidationInterceptor acts as single entry point for all Incoming Http Requests.
 * It performs authentication and authorization.
 *
 * @author Vas
 * @version 1.0
 */
@Component
public class RESTValidationInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(RESTValidationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) {
    }
}
