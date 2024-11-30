package com.server.storefront.configs;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    private static final String REGION = "ap-south-1";

    @Bean
    public AmazonS3 getS3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(REGION)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
    }
}
