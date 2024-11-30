package com.server.storefront.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AmazonS3Service {

    boolean validateBucket(String bucketName);

    String upload(MultipartFile file, String userName) throws IOException;
}
