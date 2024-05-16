package com.website.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.website.application.entity.User;
import com.website.application.service.HiService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    Log log = LogFactory.getLog(LoginController.class);

    @Autowired
    HiService hiService;
    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/login")
    public String login() throws Exception{
        return "login";
    }

    @RequestMapping("/index")
    @ResponseBody
    public String success(HttpServletResponse response) throws Exception{
        Map map = new HashMap();
        map.put("status","success");
        map.put("message","登录成功！");
        return objectMapper.writeValueAsString(map);
    }

    @RequestMapping("/errorlogin")
    @ResponseBody
    public String error(HttpServletResponse response) throws Exception{
        Map map = new HashMap();
        map.put("status","fail");
        map.put("message","登录失败！");
        return objectMapper.writeValueAsString(map);
    }

    @RequestMapping("/exitlogin")
    @ResponseBody
    public String exit(HttpServletResponse response) throws Exception{
        Map map = new HashMap();
        map.put("status","exit");
        map.put("message","退出登录！");
        return objectMapper.writeValueAsString(map);
    }

    @RequestMapping("/get/{id}")
    @ResponseBody
    public String getUser(@PathVariable String id) throws Exception{
        User user = hiService.getUser(id);
        if(user ==null){
            return "没有检索到数据";
        }
        return objectMapper.writeValueAsString(user);
    }

}
