package com.server.storefront.service;

import com.server.storefront.auth.model.UserBankDetails;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {

    UserBankDetails getUserPaymentDetails(UserBankDetails userBankDetails, String userId, HttpServletRequest request);
}
