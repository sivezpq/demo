package com.springoauth.controller;

import com.common.response.Response;
import com.springoauth.security.SecurityUserDetails;
import com.springoauth.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
public class LoginController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    @ResponseBody
    public Response login(String username, String password) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
            //如果认证没通过，给出对应的提示
            if(Objects.isNull(authenticate)){
                return Response.fail(402, "账户或密码错误");
            }
        } catch (AuthenticationException e) {
            //认证没通过，给出对应的提示
            return Response.fail(402, "账户或密码错误");
        }

        //如果认证通过了，使用username生成一个token，并存入ResponseResult返回
        SecurityUserDetails loginUser = (SecurityUserDetails) authenticate.getPrincipal();
        String token = jwtTokenUtil.generateToken(loginUser);
        Map<String,String> map = new HashMap<>();
        map.put("token", token);
        //把完整的用户信息存入redis  userid作为key
        //redisCache.setCacheObject("login:"+userid, loginUser);
        return Response.success().setData(map);
    }

    //登出接口
    @GetMapping("/user/logout")
    @ResponseBody
    public Response logout(){
        //获取用户信息
        SecurityUserDetails userInfo = this.getUserInfo();
        //TODO 删除redis中的用户信息
        //redisCache.deleteObject("login:"+userInfo.getUserName());
        return Response.success();
    }

    private SecurityUserDetails getUserInfo() {
        //获取SecurityContextHolder中的用户信息
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (SecurityUserDetails) authentication.getPrincipal();
    }

}
