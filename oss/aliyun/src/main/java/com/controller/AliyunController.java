// package com.controller;
//
// import com.sensteed.oss.aliyun.bean.MultipartCompleteRequest;
// import com.sensteed.oss.aliyun.bean.MultipartCompleteResponse;
// import com.sensteed.oss.aliyun.bean.MultipartUploadRequest;
// import com.sensteed.oss.aliyun.bean.MultipartUploadResponse;
// import com.sensteed.oss.aliyun.client.OssTemplate;
// import org.apache.http.impl.client.CloseableHttpClient;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.ResponseBody;
//
// import javax.annotation.Resource;
// import javax.servlet.http.HttpServletRequest;
// import org.apache.http.HttpEntity;
// import org.apache.http.client.methods.CloseableHttpResponse;
// import org.apache.http.client.methods.HttpPut;
// import org.apache.http.entity.FileEntity;
// import org.apache.http.impl.client.CloseableHttpClient;
// import org.apache.http.impl.client.HttpClients;
// import java.io.*;
// import java.net.URL;
// import java.util.*;
//
// /**
//  * @Desc: 测试阿里云OSS接口处理类
//  */
// @Controller
// @RequestMapping("/aliyun")
// public class AliyunController {
//
//     private static final Logger logger = LoggerFactory.getLogger(AliyunController.class);
//
//     @Resource
//     OssTemplate ossTemplate;
//
//     @GetMapping(value = "/putObject")
//     @ResponseBody
//     public String putObject(HttpServletRequest request) throws Exception{
//         String objectKey = "test/20230506.txt";
//         return ossTemplate.put(objectKey, "sdmewl地方么了对吗里540的双方的矛盾v，但是的两个，人类g".getBytes());
//     }
//
//     @GetMapping(value = "/presignedUploadUrl")
//     @ResponseBody
//     public String presignedUploadUrl(HttpServletRequest request) throws Exception{
//         String objectKey = "logs/12345678900987654/ECU/20260311121212123-20260315121212123/20230506.logs";
//         return ossTemplate.presignedUploadUrl(objectKey);
//     }
//
//     @GetMapping(value = "/uploadUrl")
//     @ResponseBody
//     public void uploadUrl(HttpServletRequest request, String url, String pathName) throws Exception{
//         this.upload(url, pathName);
//     }
//
//     @GetMapping(value = "/getContent")
//     @ResponseBody
//     public String getContent(HttpServletRequest request) throws Exception{
//         String objectKey = "test/20230506.txt";
//         return new String(ossTemplate.getContent(objectKey), "UTF-8");
//     }
//
//     @GetMapping(value = "/presignedDownloadUrl")
//     @ResponseBody
//     public String presignedDownloadUrl(HttpServletRequest request) throws Exception{
//         String objectKey = "test/20230506.txt";
// //        return ossTemplate.presignedDownloadUrl(objectKey);
//         return ossTemplate.presignedDownloadUrl(objectKey, "2eer.md", true);
//     }
//
//     @GetMapping(value = "/delete")
//     @ResponseBody
//     public String delete(HttpServletRequest request) throws Exception{
//         String objectKey = "test/20230506.txt";
// //        return ossTemplate.presignedDownloadUrl(objectKey);
//         return ossTemplate.delete(objectKey) ? "删除成功" : "删除失败";
//     }
//
//     @GetMapping(value = "/initMultipartUpload")
//     @ResponseBody
//     public MultipartUploadResponse initMultipartUpload(HttpServletRequest request, int partCount) throws Exception{
//         String objectKey = "logs/12345678900987654/ECU/20260311121212123-20260315121212123/20260311.logs";
// //        return ossTemplate.presignedDownloadUrl(objectKey);
//         MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(objectKey, partCount);
//         return ossTemplate.initMultipartUpload(multipartUploadRequest);
//     }
//
//     @GetMapping(value = "/completeMultipartUpload")
//     @ResponseBody
//     public MultipartCompleteResponse completeMultipartUpload(HttpServletRequest request, int partCount) throws Exception{
//         String objectKey = "logs/12345678900987654/ECU/20260311121212123-20260315121212123/20260311.logs";
// //        return ossTemplate.presignedDownloadUrl(objectKey);
//         MultipartCompleteRequest multipartComplete = new MultipartCompleteRequest();
//         multipartComplete.setObjectName(objectKey);
//         return ossTemplate.completeMultipartUpload(multipartComplete);
//     }
//
//     private void upload(String url, String pathName)  throws Exception {
//         CloseableHttpClient httpClient = null;
//         CloseableHttpResponse response = null;
//
//         // 将<signedUrl>替换为授权URL。
//         URL signedUrl = new URL(url);
//
//         try {
//             HttpPut put = new HttpPut(signedUrl.toString());
//             HttpEntity entity = new FileEntity(new File(pathName));
//             put.setEntity(entity);
//             httpClient = HttpClients.createDefault();
//             response = httpClient.execute(put);
//
//             logger.info("返回上传状态码: {}", response.getStatusLine().getStatusCode());
//             if(response.getStatusLine().getStatusCode() == 200){
//                 logger.info("使用网络库上传成功");
//             }
//             logger.info(response.toString());
//         } catch (Exception e){
//             logger.error("上传文件报错", e);
//         } finally {
//             if (response != null) {null
//                 response.close();
//             }
//             if (httpClient != null) {
//                 httpClient.close();
//             }
//         }
//     }
// }
