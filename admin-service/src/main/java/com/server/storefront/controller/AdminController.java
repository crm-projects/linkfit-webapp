package com.server.storefront.controller;

import com.server.storefront.model.Plan;
import com.server.storefront.service.AdminService;
import com.server.storefront.utils.Util;
import com.server.storefront.utils.json.AbstractJsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/savePlanDetails", method = RequestMethod.POST)
    public @ResponseBody AbstractJsonResponse<Boolean> savePlanDetails(@RequestBody Plan plan, HttpServletRequest request) {
        try {
            return Util.getJsonResponse(request, adminService.savePlanDetails(plan));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
