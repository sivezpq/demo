package com.bsc;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 白山云AWS配置
 * @author: huagang
 * @date: 2023年4月28日
 * @version: 1.0
 */
@Slf4j
@Configuration
public class BscConfig {

    /**
     * 白山云AWS客户端
     * @return
     */
    @Bean
    public BscClient bscClient(BscProperties awsProperties) {
        // 凭证配置
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(
                awsProperties.getAccessKey(), awsProperties.getSecretKey());

        ClientConfiguration clientconfiguration = new ClientConfiguration();
        // clientconfiguration.setMaxConnections(awsProperties.getMaxConnections());
        clientconfiguration.setSocketTimeout(60 * 60 * 1000); // in milliseconds
        clientconfiguration.setConnectionTimeout(60 * 60 * 1000); // in milliseconds

        AmazonS3 s3 = new AmazonS3Client(awsCreds, clientconfiguration);

        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
        s3.setEndpoint(awsProperties.getEndpoint());
        s3.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true)
                .disableChunkedEncoding().build()); //暂不支持ChunkedEncoding

        log.info(String.format("bsc client init success, accessKey: %s, secretKey: %s, bucketName: %s, endpoint: %s",
                awsProperties.getAccessKey(), awsProperties.getSecretKey(), awsProperties.getBucketName(),
                awsProperties.getEndpoint()));

        return new BscSimpleClient(s3);
    }

}
