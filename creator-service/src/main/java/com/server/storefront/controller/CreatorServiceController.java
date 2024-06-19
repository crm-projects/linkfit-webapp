package com.server.storefront.controller;

import com.server.storefront.creator.model.Creator;
import com.server.storefront.creator.model.CreatorLite;
import com.server.storefront.service.CreatorService;
import com.server.storefront.utils.Util;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.auth.SignIn;
import com.server.storefront.utils.json.AbstractJsonResponse;
import com.server.storefront.auth.SignUp;
import jakarta.servlet.ServletException;
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
public class CreatorServiceController {

    private static final Logger logger = LoggerFactory.getLogger(CreatorServiceController.class);

    @Autowired
    CreatorService creatorService;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public @ResponseBody AbstractJsonResponse<SignUp> authenticateCreator(@RequestBody SignUp authObj, HttpServletRequest request)
            throws CreatorException, ServletException, Exception {
        logger.info("Preparing Authentication for user: {}", authObj.getUserName());
        try {
            if (authObj == null) {
                throw new ServletException("No authObj Supplied");
            }
            return Util.getJsonResponse(request, creatorService.creatorSignUp(authObj));
        } catch (CreatorException | ServletException ex) {
            logger.error("Error: {} while authenticating User: {}", ex.getMessage(), authObj.getUserName());
            throw new CreatorException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody AbstractJsonResponse<Creator> creatorLogin(@RequestBody SignIn authObj, HttpServletRequest request)
            throws CreatorException, ServletException, Exception {
        logger.info("Preparing Login for user: {}", authObj.getUserEmail());
        try {
            if (authObj == null) {
                throw new ServletException("No authObj Supplied");
            }
            return Util.getJsonResponse(request, creatorService.creatorSignIn(authObj));
        } catch (ServletException ex) {
            logger.error("Error: {} while authenticating User: {}", ex.getMessage(), authObj.getUserEmail());
            throw new CreatorException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/saveCreatorProfileSettings", method = RequestMethod.POST)
    public @ResponseBody AbstractJsonResponse<Creator> saveCreatorProfileLiteSettings(@RequestBody CreatorLite creatorLite, HttpServletRequest request)
            throws CreatorException, ServletException, Exception {
        try {
            if (creatorLite == null) {
                throw new ServletException("No Object Supplied");
            }
            return Util.getJsonResponse(request, creatorService.saveCreatorProfile(creatorLite));
        } catch (ServletException ex) {
            throw new CreatorException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/deleteCreator", method = RequestMethod.POST)
    public @ResponseBody AbstractJsonResponse<Creator> deleteCreatorProfile(@RequestBody CreatorLite creatorLite, HttpServletRequest request)
            throws ServletException, Exception {
        try {
            if (creatorLite == null) {
                throw new ServletException("No Object Supplied");
            }
            return Util.getJsonResponse(request, creatorService.deleteCreatorProfile(creatorLite));
        } catch (ServletException ex) {
            throw new CreatorException(ex.getMessage());
        }
    }



}
