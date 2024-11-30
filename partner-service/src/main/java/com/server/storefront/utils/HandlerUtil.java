package com.server.storefront.utils;

import com.server.storefront.constants.PartnerExceptionConstants;
import com.server.storefront.exception.HandlerException;
import com.server.storefront.handler.BaseHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class HandlerUtil {

    private final Map<String, BaseHandler> handlerMap;

    public HandlerUtil(Map<String, BaseHandler> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public BaseHandler getHandler(String name) throws HandlerException {
        if (!StringUtils.hasText(name)) {
            throw new HandlerException(PartnerExceptionConstants.INVALID_INPUT);
        }
        BaseHandler handler = handlerMap.get(name.toUpperCase());
        if (handler != null) {
            return handler;
        } else {
            throw new HandlerException(PartnerExceptionConstants.HANDLER_NOT_FOUND);
        }
    }
}
