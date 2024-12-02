package com.server.storefront.service;

import com.server.storefront.model.Waitlist;
import com.server.storefront.repository.WaitlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitlistService {

    private final WaitlistRepository waitlistRepository;

    @Transactional
    public boolean addUser(Waitlist waitlist) {
        waitlistRepository.save(waitlist);
        return Boolean.TRUE;
    }
}
