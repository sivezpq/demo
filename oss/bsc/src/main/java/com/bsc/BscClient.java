package com.bsc;


import java.io.InputStream;
import java.util.List;

/**
 * 白山云存储对象AWS接口
 * @author: huagang
 * @date: 2023年4月28日
 * @version: 1.0
 */

public interface BscClient {

    /**
     * 基于文件夹上传对象
     * @param folderName
     * @param objectKey
     * @param input
     * @return url 对象访问URL
     */
    public String putObject(String folderName, String objectKey, InputStream input);

    /**
     * 上传对象
     * @param objectKey
     * @param input
     *
     * @return url 对象访问URL
     */
    public String putObject(String objectKey, InputStream input);

    /**
     * 上传本地文件
     * @param objectKey
     * @param localFile
     * @return url 对象访问URL
     */
    public String putObject(String objectKey, String localFile);

    /**
     * 上传本地文件
     * @param objectKey
     * @param localFile
     * @return url 对象访问URL
     */
    public String putObjectTmp(String objectKey, String localFile);

    /**
     * 删除
     * @param objectKey
     * @return
     */
    public boolean deleteObject(String objectKey);

    /**
     * 白山云存储对象的防盗链：获取已签名的URL，默认配置的过期时间（毫秒）
     *
     * @param objectKey
     * @return url 对象访问URL
     */
    public String getOssSignatureUrl(String objectKey);

    /**
     * 白山云存储对象的防盗链：获取已签名的URL，自定义过期时间（毫秒）
     *
     * @param objectKey
     * @param expireTime 过期时间（毫秒）
     * @return url 对象访问URL
     */
    public String getOssSignatureUrl(String objectKey, long expireTime) ;

    /**
     * 白山云CDN的防盗链：获取已签名的URL
     *
     * @param objectKey
     * @return url CDN防盗链URL
     */
    public String getCdnSignatureUrl(String objectKey);

    /**
     * cdn刷新缓存
     * @param urls 白山云CDN的urls
     * @return result  白山云cdn刷新缓存接口返回结果
     *{
     *     "code": 0,  //code为0，表示数据返回正常，非0时则不正常
     *     "data": {
     *         "task_id": "2400003",//本次提交的任务id, 可根据此id查询本次成功提交推送的url/dir状态
     *         "count": 4 //本次成功提交的url数量,
     *         err_urls: 该字段信息返回时, 主要原因如下:
     *         //1. url格式不符合规则
     *         //2. url域名必须为该账号下的域名
     *         //3. 域名可能为挂起或删除状态
     *         "err_urls":["http://xxxx.com/xx","http://test.com/xxx"]
     *     }
     * }
     */
    public CdnResult<CdnRefresh> cdnRefresh(List<String> urls) ;
}
