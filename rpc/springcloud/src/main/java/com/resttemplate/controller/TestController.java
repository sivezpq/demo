package com.resttemplate.controller;

import com.common.response.Response;
import com.resttemplate.service.RestTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Desc: 测试接口处理类
 */
@Controller
@RequestMapping("/resttemplate")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    RestTemplateService restTemplateService;
    /**
     * post
     *
     * @param request
     * @param url
     * @return
     */
    @PostMapping(value = "/post")
    @ResponseBody
//    public Response post(HttpServletRequest request, @RequestBody @Validated String param) {
    public Response post(HttpServletRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> entity = restTemplateService.post(url, httpEntity, Map.class);
        Map result = entity.getBody();
        return Response.success(result);
    }

    /**
     * 验证spring-cloud-netflix-core引起的内存溢出
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/netflix")
        @ResponseBody
        public Response netflix(HttpServletRequest request) {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 3, 3, 4, 4, 4, 5, 6, 78));
        List<Integer> collect = list.stream()
                //先将偶数筛选，再去重，返回集合
                .filter(s -> s % 2 == 0)
                .distinct()
                .collect(Collectors.toList());

        String date = String.valueOf(System.currentTimeMillis());
            System.out.println("continue");
            String url =  "http://netflix-test.com/test/init?date=" + date+"&timeStr="+date;
        ResponseEntity<String> entity = restTemplateService.get(url, String.class);
        return Response.success();
    }

}
