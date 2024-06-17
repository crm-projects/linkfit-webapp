package com.server.storefront.controller;

import com.server.storefront.creator.Creator;
import com.server.storefront.creator.CreatorLite;
import com.server.storefront.service.CreatorService;
import com.server.storefront.utils.Util;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.utils.json.AbstractJsonResponse;
import com.server.storefront.utils.holder.SignUp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
    public @ResponseBody SignUp authenticateCreator(@RequestBody SignUp authObj, HttpServletRequest request)
            throws CreatorException, ServletException, Exception {
        logger.info("Preparing Authentication for user: {}", authObj.getUserName());
        try {
            if (authObj == null) {
                throw new ServletException("No authObj Supplied");
            }
            return creatorService.authenticateCreator(authObj);
        } catch (CreatorException | ServletException ex) {
            logger.error("Error: {} while authenticating User: {}", ex.getMessage(), authObj.getUserName());
            throw new CreatorException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/saveCreatorProfileSettings", method = RequestMethod.POST)
    public @ResponseBody AbstractJsonResponse<Creator> saveCreatorProfileLiteSettings(@RequestBody CreatorLite creatorLite, HttpServletRequest request)
            throws CreatorException, ServletException, Exception {
        try {
            if ( creatorLite == null) {
                throw new ServletException("No Object Supplied");
            }
            boolean isExistingCreator = StringUtils.hasText(creatorLite.getCreatorId());
            return Util.getJsonResponse(request, creatorService.saveUpdateCreatorProfileSettings(creatorLite, isExistingCreator));
        } catch (CreatorException | ServletException ex) {
            throw new CreatorException(ex.getMessage());
        }
    }

}
