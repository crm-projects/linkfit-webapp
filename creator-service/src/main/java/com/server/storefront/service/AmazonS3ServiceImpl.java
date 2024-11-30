package com.server.storefront.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3ServiceImpl implements AmazonS3Service {

    private final AmazonS3 s3;


    @Override
    public List<Bucket> getBucketList() {
        log.info("Fetching Bucket List from S3");
        return s3.listBuckets();
    }

    @Override
    public boolean validateBucket(String bucketName) {
        List<Bucket> buckets = getBucketList();
        log.info("Bucket List : {}", buckets);
        return buckets.stream().anyMatch(bucket -> bucketName.equals(bucket.getName()));
    }

    @Override
    public void upload(String bucketName, String key, File file) {

    }
}
