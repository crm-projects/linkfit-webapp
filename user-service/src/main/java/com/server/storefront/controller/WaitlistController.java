package com.server.storefront.controller;

import com.server.storefront.model.Waitlist;
import com.server.storefront.service.WaitlistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "/waitlist")
public class WaitlistController {

    private final WaitlistService waitlistService;

    @PostMapping(value = "/signup")
    public ResponseEntity<Boolean> addUserToWailist(@RequestBody Waitlist waitlist, HttpServletRequest request) {
        return ResponseEntity.ok(waitlistService.addUser(waitlist));
    }
}
