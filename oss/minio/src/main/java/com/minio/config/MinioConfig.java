package com.minio.config;

import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * minio配置类
 *
 */
@Component
public class MinioConfig {

    private static final Logger logger = LoggerFactory.getLogger(MinioConfig.class);

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioManager minioManager(){
        SimpleMinioManager minioManager = new SimpleMinioManager();
        try {
            //创建minio实例
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getUrl())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();
            //链接测试
            List<Bucket> buckets = minioClient.listBuckets();
            List<String> bucketNames = buckets.stream().map(Bucket::name).collect(Collectors.toList());
            boolean contains = bucketNames.contains(minioProperties.getBucketName());
            if (!contains) {
                logger.error("minio Client Bucket not contains " + minioProperties.getBucketName()
                        + ", Client build ERROR, Please Check your bucketName");
            }
            logger.info("minio client build SUCCESS");
            minioManager.setMinioClient(minioClient);
        } catch (Exception e) {
            logger.error("minio client build error {}", e.getMessage(), e);
        }
        minioManager.setBucketName(minioProperties.getBucketName());
        return minioManager;
    }

}
