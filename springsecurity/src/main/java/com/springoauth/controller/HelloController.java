package com.springoauth.controller;

import com.common.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Value("${spring.application.name:local}")
    private String applicationname;

    @GetMapping("haha")
    public Response haha() {
        return Response.success().setData("haha");
    }

    @GetMapping("haha1")
    public Response haha1() {
        return Response.success().setData("haha1");
    }

    @GetMapping("callback")
    public Response callback(String code) {
        return Response.success().setData(code);
    }
}
