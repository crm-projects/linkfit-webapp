package com.server.storefront.controller;

import com.server.storefront.model.creator.Creator;
import com.server.storefront.model.creator.CreatorLite;
import com.server.storefront.service.CreatorService;
import com.server.storefront.utils.Util;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.model.auth.SignIn;
import com.server.storefront.utils.json.AbstractJsonResponse;
import com.server.storefront.model.auth.SignUp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.log;
import org.slf4j.logFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/creator")
@Slf4j
public class CreatorServiceController {

    @Autowired
    CreatorService creatorService;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public AbstractJsonResponse<SignUp> creatorSignUp(@RequestBody SignUp authObj, HttpServletRequest request)
            throws CreatorException, ServletException, Exception {
        log.info("Preparing Authentication for user: {}", authObj.getUserName());
        try {
            if (authObj == null) {
                throw new ServletException("No authObj Supplied");
            }
            return Util.getJsonResponse(request, creatorService.creatorSignUp(authObj));
        } catch (CreatorException | ServletException ex) {
            log.error("Error: {} while authenticating User: {}", ex.getMessage(), authObj.getUserName());
            throw new CreatorException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AbstractJsonResponse<Creator> creatorLogin(@RequestBody SignIn authObj, HttpServletRequest request)
            throws CreatorException, ServletException, Exception {
        log.info("Preparing Login for user: {}", authObj.getUserEmail());
        try {
            if (authObj == null) {
                throw new ServletException("No authObj Supplied");
            }
            return Util.getJsonResponse(request, creatorService.creatorSignIn(authObj));
        } catch (ServletException ex) {
            log.error("Error: {} while authenticating User: {}", ex.getMessage(), authObj.getUserEmail());
            throw new CreatorException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/saveCreatorProfileSettings", method = RequestMethod.POST)
    public AbstractJsonResponse<Creator> saveCreatorProfileLiteSettings(@RequestBody CreatorLite creatorLite, HttpServletRequest request)
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
    public AbstractJsonResponse<Creator> deleteCreatorProfile(@RequestBody CreatorLite creatorLite, HttpServletRequest request)
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
