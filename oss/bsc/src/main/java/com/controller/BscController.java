package com.controller;

import com.bsc.BscClient;
import com.common.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 测试白山云接口处理类
 */
@Controller
@RequestMapping("/bsc")
public class BscController {

    private static final Logger logger = LoggerFactory.getLogger(BscController.class);

    @Resource
    BscClient bscClient;

    @GetMapping(value = "/putObject")
    @ResponseBody
    public Response putObject(HttpServletRequest request) throws Exception{
        String objectKey = "test/20230506.png";
        bscClient.putObject(objectKey, "D:/20230516.png");
        String url = bscClient.getCdnSignatureUrl(objectKey);
        return Response.success(url);
    }

    @GetMapping(value = "/deleteObject")
    @ResponseBody
    public Response deleteObject(HttpServletRequest request, String objectKey) throws Exception{
        String result = bscClient.deleteObject(objectKey)?"SUCCESS":"FALSE";
        return Response.success(result);
    }

    @GetMapping(value = "/cdnurl")
    @ResponseBody
    public Response cdnurl(HttpServletRequest request, String objectKey) throws Exception{
        String url = bscClient.getCdnSignatureUrl(objectKey);
        return Response.success(url);
    }

    @GetMapping(value = "/cdnRefresh")
    @ResponseBody
    public Response cdnRefresh(HttpServletRequest request, String url) throws Exception{
//        String url = "https://hothill-cdn-test.autoai.com/test/20230504.png";
        List<String> list = new ArrayList<String>();
        list.add(url);
        return Response.success(bscClient.cdnRefresh(list));
    }
}
