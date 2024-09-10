package com.server.storefront.holder;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
public class OTPHolder {

    public Map<String,String> emailOTPHolder = new HashMap<>();

    @Cacheable(value = "emailOTPCache")
    public String getOTPByEmailAddress(String emailAddress ) {
        return emailOTPHolder.get(emailAddress);
    }

    @CachePut(value = "emailOTPCache")
    public void updateEmailOTPMap(String email, String OTP) {
        emailOTPHolder.put(email, OTP);
    }

}
