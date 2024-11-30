package com.server.storefront.configs;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    private final AmazonS3 s3Client;

    private static final String AP_SOUTH = "ap-south-1";

    public S3Config() {
        this.s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(AP_SOUTH)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
    }

    public AmazonS3 getS3Client() {
        return s3Client;
    }
}
