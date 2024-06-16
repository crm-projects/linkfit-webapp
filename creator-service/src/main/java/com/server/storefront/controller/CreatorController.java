package com.server.storefront.controller;

import com.server.storefront.model.Creator;
import com.server.storefront.model.CreatorLite;
import com.server.storefront.service.CreatorService;
import com.server.storefront.utils.Util;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.utils.json.AbstractJsonResponse;
import com.server.storefront.utils.holder.SignUp;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/creator")
public class CreatorController {

    private static final Logger logger = LoggerFactory.getLogger(CreatorController.class);

    @Autowired
    CreatorService creatorService;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public @ResponseBody SignUp authenticateCreator(@RequestBody SignUp signUp, HttpServletRequest request)
            throws CreatorException {
        logger.info("Preparing Authentication for user: {}", signUp.getUserName());
        try {
            return creatorService.authenticateCreator(signUp);
        } catch (Exception ex) {
            logger.error("Error: {} while authenticating User: {}", ex.getMessage(), signUp.getUserName());
            throw new CreatorException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/saveCreatorProfileLiteSettings", method = RequestMethod.POST)
    public @ResponseBody AbstractJsonResponse<Creator> saveCreatorProfileLiteSettings(@RequestBody CreatorLite creatorLite, HttpServletRequest request)
            throws CreatorException {
        try {
            return Util.getJsonResponse(request, creatorService.saveUpdateCreatorProfileSettings(creatorLite));
        } catch (CreatorException ex) {
            throw new CreatorException(ex.getMessage());
        }
    }

}
