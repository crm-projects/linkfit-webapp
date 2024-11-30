package com.server.storefront.service;

import com.amazonaws.services.s3.model.Bucket;

import java.io.File;
import java.util.List;

public interface AmazonS3Service {

    List<Bucket> getBucketList();

    boolean validateBucket(String bucketName);

    void upload(String bucketName, String key, File file);
}
