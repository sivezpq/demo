package com.bsc;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 白山云AWS简单操作辅助工具
 * @author: huagang
 * @date: 2023年4月28日
 * @version: 1.0
 */
@Slf4j
public class BscSimpleClient implements BscClient {

    /**
     * 客户端
     */
    private AmazonS3 s3Client;

    @Resource
    public BscProperties awsProperties;


    public BscSimpleClient(AmazonS3 s3Client){
        this.s3Client = s3Client;
    }

    /**
     * 基于文件夹上传对象
     * @param folderName
     * @param objectKey
     * @param input
     * @return url 对象访问URL
     */
    @Override
    public String putObject(String folderName, String objectKey, InputStream input){
        String objectKey0 = formatFolderName(folderName) + objectKey;
        return putObject(objectKey0, input);
    }

    /**
     * 上传对象
     * @param objectKey
     * @param input
     *
     * @return url 对象访问URL
     */
    @Override
    public String putObject(String objectKey, InputStream input){
        log.info(String.format("put object by key(%s) ...", objectKey));
        //获取对象元数据
        ObjectMetadata objectMetadata = getObjectMetadata(input);
        //对象请求
        PutObjectRequest putObjectrequest = new PutObjectRequest(awsProperties.getBucketName(), objectKey,
                input, objectMetadata);
        return this.put(objectKey, putObjectrequest);
    }

    /**
     * 上传本地文件
     * @param objectKey
     * @param localFile
     * @return url 对象访问URL
     */
    @Override
    public String putObject(String objectKey, String localFile){
        log.info(String.format("put object by key: %s，localFile: %s", objectKey, localFile));
        //对象请求
        PutObjectRequest putObjectrequest = new PutObjectRequest(awsProperties.getBucketName(), objectKey,
                new File(localFile));
        return this.put(objectKey, putObjectrequest);
    }

    @Override
    public String putObjectTmp(String objectKey, String localFile) {
        log.info(String.format("put object tmp by key: %s，localFile: %s", objectKey, localFile));
        //对象请求
        PutObjectRequest putObjectrequest = new PutObjectRequest(awsProperties.getBucketName(), objectKey,
                new File(localFile));
        putObjectrequest.setSdkRequestTimeout(1);
        return this.put(objectKey, putObjectrequest);
    }

    /**
     * 上传对象到白山云
     * @param objectKey
     * @param putObjectrequest
     * @return url 对象访问URL
     */
    private String put(String objectKey, PutObjectRequest putObjectrequest) {
        //指定对象ACL
        putObjectrequest.setCannedAcl(CannedAccessControlList.PublicRead);
        //上传对象
        PutObjectResult putObjectResult = s3Client.putObject(putObjectrequest);
        //上传对象url
        String url = awsProperties.getDomain() + File.separator + objectKey;
        log.info(String.format("put object success, url: %s", url));
        return url;
    }

    /**
     * 删除
     * @param objectKey
     * @return
     */
    @Override
    public boolean deleteObject(String objectKey) {
        boolean exist = s3Client.doesObjectExist(awsProperties.getBucketName(), objectKey);
        if (!exist) {
            return true;
        }
        s3Client.deleteObject(awsProperties.getBucketName(), objectKey);
        return true;
    }

    /**
     * 白山云存储对象的防盗链：获取已签名的URL，默认配置的过期时间（毫秒）
     * @param objectKey
     * @return url 对象访问URL
     */
    @Override
    public String getOssSignatureUrl(String objectKey) {
        return this.getOssSignatureUrl(objectKey, awsProperties.getExpireTime());
    }

    /**
     * 白山云存储对象的防盗链：获取已签名的URL，自定义过期时间（毫秒）
     * @param objectKey
     * @return url 对象访问URL
     */
    @Override
    public String getOssSignatureUrl(String objectKey, long expireDate) {
        if (StringUtils.isEmpty(objectKey)) {
            return Strings.EMPTY;
        }
        Date date = this.getExpireDate(expireDate);
        URL url = s3Client.generatePresignedUrl(awsProperties.getBucketName(), objectKey,
                date);
        log.info(String.format("objectKey: %s，signature url: %s，expireDate: %s，date: %s", objectKey, url.toString(), expireDate, date));
        return url.toString();
    }

    /**
     * 白山云CDN的防盗链：获取已签名的URL
     * @param objectKey
     * @return url CDN防盗链URL
     */
    @Override
    public String getCdnSignatureUrl(String objectKey) {
        if (StringUtils.isEmpty(objectKey)) {
            return Strings.EMPTY;
        }
        try {
            StringBuffer url = new StringBuffer(100);
            url.append(awsProperties.getDomain());
            url.append(File.separator).append(objectKey);
            /**
             * CDN加时间戳防盗链，即使不是防盗链目录也可以加防盗链参数
             * MD5加密串组合方式：$key$uri$time   time：10进制时间戳
             * 防盗链url格式：https://$domain/$uri?secret=$md5&time=$time
             */
            long time = System.currentTimeMillis();
            String md5Str = awsProperties.getCdnSignatureKey()+"/"+objectKey+time;
            String secret = this.generateMD5(md5Str);
            url.append("?secret=").append(secret);
            url.append("&time=").append(time);
            log.info(String.format("objectKey: %s，url: %s", objectKey, url.toString()));
            return url.toString();
        } catch(Exception ex){
            throw new BscException("获取白山云cdn防盗链地址失败", ex);
        }
    }

    /**
     * cdn刷新缓存
     * @param urls 白山云存储对象urls
     * @return result  白山云cdn刷新缓存接口返回结果
     */
    @Override
    public CdnResult<CdnRefresh> cdnRefresh(List<String> urls) {
        Map<String, Object> json = new HashMap<String, Object>(2);
        json.put("urls", urls);
        json.put("type", "url");
        try{
            ObjectMapper mapper = new ObjectMapper();
            String cdnUrl = awsProperties.getCdnUrl()+"?token="+awsProperties.getCdnToken();
            String body = mapper.writeValueAsString(json);
            log.info("请求CDN刷新缓存接口参数：url:{},body:{}", cdnUrl, body);
            String reponseData = this.postJson(cdnUrl, body);
            log.info("请求CDN刷新缓存接口结果：result:{}", reponseData);
            CdnResult<CdnRefresh> result =mapper.readValue(reponseData, new TypeReference<CdnResult<CdnRefresh>>(){});
            return result;
        }catch(Exception e){
            throw new BscException("白山云cdn刷新缓存失败", e);
        }
    }

    /**
     * 格式化文件夹名
     * @param folderName
     * @return
     */
    private String formatFolderName(String folderName){
        String objectKey = folderName;
        if(!objectKey.endsWith("/")){
            objectKey = objectKey + "/";
        }
        return objectKey;
    }

    /**
     * 获取MD5值
     * @param input
     * @return
     */
    private String generateMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashText = no.toString(16);
        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }
        return hashText;
    }

    /**
     * 获取对象元数据
     * @param input
     * @return
     */
    private ObjectMetadata getObjectMetadata(InputStream input){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try{
            objectMetadata.setContentLength(Long.valueOf(input.available()));
        }catch(IOException e){
            throw new BscException("白山云上传文件失败", e);
        }
        return objectMetadata;
    }

    /**
     * 获取过期时间
     */
    private Date getExpireDate(long expireDate){
        return new Date(System.currentTimeMillis() + expireDate);
    }

    /**
     * 发送第三方cdn刷新缓存api
     * @param url  cdn地址
     * @return json post数据
     */
    private String postJson(String url, String body) throws Exception {
        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setReadTimeout(30000);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("accept", "application/json");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        conn.setRequestProperty("token", awsProperties.getCdnToken());

        if (!StringUtils.isEmpty(body)) {
            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes("UTF-8"));
            os.close();
        }

        conn.connect();

        int code = conn.getResponseCode();
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            if (code == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }

            String msg;
            while ((msg = br.readLine()) != null) {
                sb.append(msg).append("\n");
            }

            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }

        return sb.toString();
    }

}
