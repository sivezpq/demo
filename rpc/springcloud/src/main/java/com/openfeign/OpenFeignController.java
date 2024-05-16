package com.openfeign;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("/openfeign")
public class OpenFeignController {

    private static final Logger logger = LoggerFactory.getLogger(OpenFeignController.class);

    @Autowired
    OpenFeignService openFeignService;
    /**
     * post
     *
     * @return
     */
    @PostMapping(value = "/feign")
    @ResponseBody
    public Response post() {
        return Response.success(openFeignService.test());
    }

}
