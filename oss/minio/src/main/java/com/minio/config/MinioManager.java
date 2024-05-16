package com.minio.config;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * minio操作接口类
 *
 */
public interface MinioManager {
    /**
     * 创建桶
     *
     * @param bucketName
     * @return true:创建成功 false:创建失败
     */
    boolean createBucket(String bucketName);

    /**
     * 上传对象
     * @param file
     * @param uploadPath
     * @return
     */
    String upload(MultipartFile file, String uploadPath);

    /**
     * 上传对象
     * @param file
     * @param uploadPath
     * @return
     */
    String upload(File file, String uploadPath);

    /**
     * 获取对象URL
     * @param objectName
     * @return
     */
    String getUrl(String objectName);
}
