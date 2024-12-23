package com.server.storefront.controller;

import com.server.storefront.model.Waitlist;
import com.server.storefront.service.WaitlistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/waitlist")
public class WaitlistController {

    private final WaitlistService waitlistService;

    @PostMapping(value = "/signup")
    public ResponseEntity<Boolean> addUserToWailist(@RequestBody Waitlist waitlist, HttpServletRequest request) {
        return ResponseEntity.ok(waitlistService.addUser(waitlist));
    }
}
