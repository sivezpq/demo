package com.minio.controller;

import com.minio.config.MinioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

/**
 * @Desc: 测试minio接口处理类
 */
@Controller
@RequestMapping("/minio")
public class MinioController {
    private static final Logger logger = LoggerFactory.getLogger(MinioController.class);
    @Resource
    MinioManager minioManager;

    @GetMapping(value = "/getUrl")
    @ResponseBody
    public String getUrl(String objectName){
        return minioManager.getUrl(objectName);
    }

    @PostMapping(value = "/upload")
    @ResponseBody
    public String upload(MultipartFile file){
        return minioManager.upload(file, "test");
    }

    @PostMapping(value = "/uploadlocalfile")
    @ResponseBody
    public String uploadlocalfile(){
        File file = new File("/Users/huagang/autoai/23MM/23mm应用商店生产环境部署资料.rar");
        return minioManager.upload(file, "test");
    }
}
