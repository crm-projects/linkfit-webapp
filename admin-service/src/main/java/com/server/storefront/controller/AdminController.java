package com.server.storefront.controller;

import com.server.storefront.model.Plan;
import com.server.storefront.model.Platform;
import com.server.storefront.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class AdminController {


    @Autowired
    AdminService adminService;

    @PostMapping("/api/plan/save")
    public ResponseEntity<Boolean> savePlanDetails(@RequestBody Plan plan, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(adminService.savePlanDetails(plan), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @PostMapping("/api/platform/save")
    public ResponseEntity<Platform> savePlatformItems(@RequestBody Platform platform, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(adminService.savePlatformItems(platform), HttpStatus.OK);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
