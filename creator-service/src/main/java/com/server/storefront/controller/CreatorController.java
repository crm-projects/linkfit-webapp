package com.server.storefront.controller;

import com.server.storefront.model.Creator;
import com.server.storefront.model.CreatorLite;
import com.server.storefront.service.CreatorService;
import com.server.storefront.utils.Util;
import com.server.storefront.utils.json.AbstractJsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/creator")
public class CreatorController {


    @Autowired
    private CreatorService creatorService;

    @RequestMapping(value = "/saveCreator", method = RequestMethod.POST)
    public @ResponseBody AbstractJsonResponse<Boolean> saveCreatorLogin(@RequestBody CreatorLite creatorLite, HttpServletRequest request) {
        try {
            return Util.getJsonResponse(request, creatorService.saveUpdateCreatorDetails(creatorLite));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(value = "/saveCreatorPlatform", method = RequestMethod.POST)
    public @ResponseBody AbstractJsonResponse<Creator> saveCreatorPlatformDetails(@RequestBody CreatorLite creatorLite, HttpServletRequest request) {
        try {
            return Util.getJsonResponse(request, creatorService.saveUpdatePlatformDetails(creatorLite));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
