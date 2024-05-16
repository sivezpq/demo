package com.bsc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 白山云AWS属性对象
 * @author: huagang
 * @date: 2023年4月28日
 * @version: 1.0
 */
@Component
@ConfigurationProperties(prefix = "com.bsc")
public class BscProperties {
    /**
     * 终端节点
     */
    private String endpoint = "https://ss.bscstorage.com";
    /**
     * AK
     */
    private String accessKey = "o4jb0g21n5mheciw9rvf";
    /**
     * SK
     */
    private String secretKey = "fbQqZ01m0SKRqW+Jr/WvBDhPkez76T6arr7nU9Lx";
    /**
     * 桶名称
     */
    private String bucketName = "dev-24mm";
    /**
     * 访问域名
     */
    private String domain = "https://ss.bscstorage.com/dev-24mm";
    /**
     * 过期时间，默认单位：秒
     */
    private long expireTime = 3600L;
    /**
     * cdn token
     */
    private String cdnUrl = "https://cdn.api.baishan.com/v2/cache/refresh";
    /**
     * cdn token
     */
    private String cdnToken = "4fba1fdb65b4736f6087da2c198fb3ac";
    /**
     * cdn防盗链目录
     */
    private String cdnSignatureUrl = "/test/.*";
    /**
     * cdn防盗链key
     */
    private String cdnSignatureKey = "autoai.20230515!";
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getCdnToken() {
        return cdnToken;
    }

    public void setCdnToken(String cdnToken) {
        this.cdnToken = cdnToken;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }

    public String getCdnSignatureUrl() {
        return cdnSignatureUrl;
    }

    public void setCdnSignatureUrl(String cdnSignatureUrl) {
        this.cdnSignatureUrl = cdnSignatureUrl;
    }

    public String getCdnSignatureKey() {
        return cdnSignatureKey;
    }

    public void setCdnSignatureKey(String cdnSignatureKey) {
        this.cdnSignatureKey = cdnSignatureKey;
    }
}
