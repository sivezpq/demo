package com.minio.config;

import io.minio.*;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * minio操作处理类
 *
 */
public class SimpleMinioManager implements MinioManager {

    private static final Logger logger = LoggerFactory.getLogger(MinioConfig.class);

    private String bucketName;

    private MinioClient minioClient;

    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }


    /**
     * 创建桶
     *
     * @param bucketName
     * @return true:创建成功 false:创建失败
     */
    @Override
    public boolean createBucket(String bucketName){
        try{
            if(this.existsBucket(bucketName)){
                logger.info("桶({})已存在", bucketName);
                return true;
            }
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
            minioClient.makeBucket(makeBucketArgs);
            //配置权限 需要手动将桶设置为公共读
            SetBucketPolicyArgs policy = SetBucketPolicyArgs.builder().bucket(bucketName).config("null").build();
            minioClient.setBucketPolicy(policy);
            logger.info("桶({})创建成功", bucketName);
            return true;
        } catch(Exception e){
            logger.error("minio创建桶是否存在报错,createBucket(),bucketName:{}", e);
            throw new MinioException("minio创建桶是否存在报错", e);
        }
    }

    /**
     * 上传对象
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file, String uploadPath) {
        try(InputStream inputStream= file.getInputStream()) {
            return this.upload(inputStream, file.getOriginalFilename(), file.getSize(), uploadPath);
        } catch(MinioException e){
            logger.error("minio上传文件报错,upload(MultipartFile file)", e);
            throw e;
        } catch(Exception e){
            logger.error("minio上传文件报错,upload(MultipartFile file)", e);
            throw new MinioException("minio上传文件报错", e);
        }
    }

    /**
     * 上传对象
     * @param file
     * @return objectName
     */
    @Override
    public String upload(File file, String uploadPath) {
        try(InputStream inputStream= new FileInputStream(file)) {
            return this.upload(inputStream, file.getName(), file.length(), uploadPath);
        } catch(MinioException e){
            logger.error("minio上传文件报错,upload(File file)", e);
            throw e;
        } catch(Exception e){
            logger.error("minio上传文件报错,upload(File file)", e);
            throw new MinioException("minio上传文件报错", e);
        }
    }

    /**
     * 获取对象URL
     * @param objectName
     * @return
     */
    @Override
    public String getUrl(String objectName) {
        try {
            Assert.notNull(minioClient, "minio client build error"); //判断minio build初始化状态
            Assert.isTrue(this.existsBucket(bucketName), "minio桶(" + bucketName + ")不存在!"); //判断桶是否存在
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName).build()).split("\\?")[0];
        } catch(IllegalArgumentException e){
            logger.error(e.getMessage(), e);
            throw new MinioException(e.getMessage(), e);
        }catch (Exception e){
            logger.error("minio访问指定文件链接报错,getUrl(String objectName),objectName:{}", objectName, e);
            throw new MinioException("minio访问指定文件链接报错", e);
        }
    }

    /**
     * 上传对象
     *
     * @param inputStream 对象内容
     * @param fileName 对象名称
     * @param fileSize 对象大小
     * @param uploadPath 上传路径
     *
     * @return objectName
     */
    private String upload(InputStream inputStream, String fileName, long fileSize, String uploadPath){
        try {
            Assert.notNull(minioClient, "minio client build error"); //判断minio build初始化状态
            Assert.isTrue(this.existsBucket(bucketName), "minio桶(" + bucketName + ")不存在!"); //判断桶是否存在
            //获取objectName，文件名是当前时间
            String objectName = this.preUpload(fileName, uploadPath);
            //上传文件
            PutObjectArgs put = PutObjectArgs.builder()
                    .stream(inputStream, fileSize, -1)
                    .bucket(bucketName).object(objectName).build();
            ObjectWriteResponse response = minioClient.putObject(put);
            logger.info("上传对象成功, objectName:{}", objectName);
            return objectName;
        } catch(IllegalArgumentException e){
            throw new MinioException(e.getMessage(), e);
        } catch(Exception e){
            throw new MinioException("minio上传文件报错", e);
        }
    }

    /**
     * 获取objectName，文件名是当前时间
     *
     * @param originalFileName
     * @return
     */
    private String preUpload(String originalFileName, String mkdir) {
//        String prefix = LocalDateTime.now().toString().replaceAll(":", "-");
//        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
//        if (mkdir == null) {
//            return prefix + suffix;
//        }
//        return mkdir + "/" + prefix + suffix;
        if (mkdir == null) {
            return originalFileName;
        }
        return mkdir + "/" + originalFileName;
    }

    /**
     * 判断桶是否存在
     *
     * @return  true:存在 false:不存在
     */
    private boolean existsBucket(String bucketName){
        try{
            BucketExistsArgs args = BucketExistsArgs.builder().bucket(bucketName).build();
            return minioClient.bucketExists(args);
        } catch(Exception e){
            throw new MinioException("minio判断桶是否存在报错", e);
        }
    }

}
