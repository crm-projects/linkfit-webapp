package com.server.storefront.service;

import com.server.storefront.model.Waitlist;
import com.server.storefront.repository.WaitlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitlistService {

    private final WaitlistRepository waitlistRepository;

    private static final String EMAIL_PATTERN = "^[a-z0-9_-]+@gmail.com$";

    @Transactional
    public boolean addUser(Waitlist waitlist) {

        if (waitlist == null) {
            throw new RuntimeException("Null object is passed.");
        }

        if (!StringUtils.hasLength(waitlist.getUserName()) ||
                (!StringUtils.hasLength(waitlist.getEmailAddress()) || waitlist.getEmailAddress().matches(EMAIL_PATTERN))) {
            throw new RuntimeException("Invalid Input details are provided. Please try again");
        }

        waitlistRepository.save(waitlist);
        return Boolean.TRUE;
    }
}
