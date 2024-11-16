package com.server.storefront.utils;

import com.server.storefront.constants.ApplicationConstants;
import com.server.storefront.constants.ProductConstants;
import com.server.storefront.exception.ProductException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ProductUtil {

    private static final char PRODUCT_IDENTIFIER = 'P';

    private static final String SHA256 = "SHA-256";

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static Date validUntil(Date now) {
        if (Objects.isNull(now))
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH, 12);
        return calendar.getTime();
    }

    public static String generateUniqueKey(String s, boolean isRootNode) throws ProductException {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA256);
            byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
            String encodedText = bytesToBase62(hash).substring(0, 6);
            return (isRootNode) ? PRODUCT_IDENTIFIER + encodedText : encodedText;
        } catch (NoSuchAlgorithmException e) {
            throw new ProductException(e.getMessage());
        }
    }

    private static String bytesToBase62(byte[] bytes) {
        BigInteger number = new BigInteger(1, bytes);
        StringBuilder base62 = new StringBuilder();
        while (number.compareTo(BigInteger.ZERO) > 0) {
            base62.insert(0, BASE62.charAt(number.mod(BigInteger.valueOf(62)).intValue()));
            number = number.divide(BigInteger.valueOf(62));
        }
        return base62.toString();
    }

    public static String getAffiliateUrl(String code) {
        return ProductConstants.AFFILIATE_BASE_URL + ApplicationConstants.PARAM_SEPARATOR_CHAR + code;
    }
}
