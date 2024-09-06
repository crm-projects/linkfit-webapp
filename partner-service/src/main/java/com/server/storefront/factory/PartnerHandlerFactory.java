package com.server.storefront.factory;

import com.server.storefront.constants.ExceptionConstants;
import com.server.storefront.exception.HandlerException;
import com.server.storefront.handler.BaseHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class PartnerHandlerFactory {

    private final Map<String, BaseHandler> handlerMap;

    public PartnerHandlerFactory(Map<String, BaseHandler> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public BaseHandler getHandler(String name) throws HandlerException {
        if (!StringUtils.hasText(name)) {
            throw new HandlerException(ExceptionConstants.INVALID_INPUT);
        }
        BaseHandler handler = handlerMap.get(name.toUpperCase());
        if (handler != null) {
            return handler;
        } else {
            throw new HandlerException(ExceptionConstants.HANDLER_NOT_FOUND);
        }
    }
}
