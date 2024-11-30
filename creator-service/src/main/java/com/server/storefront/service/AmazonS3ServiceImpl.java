package com.server.storefront.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3ServiceImpl implements AmazonS3Service {

    private final S3Client s3;

    private static final String BUCKET = "d-linkfit-bucket";

    @Override
    public boolean validateBucket(String bucketName) {
        return false;
    }

    @Override
    public String upload(MultipartFile file, String userName) throws IOException {

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String filePath = "users/" + userName + "/images/" + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(filePath)
                .build();

        // Upload the file to S3
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectResponse response = s3.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
            return "https://" + BUCKET + ".s3." + Region.AP_SOUTH_1 + ".amazonaws.com/" + filePath;
        }
    }
}
