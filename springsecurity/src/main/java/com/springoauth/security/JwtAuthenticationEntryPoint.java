package com.springoauth.security;

import com.common.response.Response;
import com.springoauth.util.JsonUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 如果访问接口请求时，请求参数中没有token凭证，则进入这里
 * @author huag
 * @date 2019/11/29 15:06
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        System.out.println("JwtAuthenticationEntryPoint:"+authException.getMessage());
        String json = JsonUtil.marshal(Response.fail(403, "没有凭证"));

        //WebUtils.renderString(response,json);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(json);
        out.flush();
        out.close();
    }
}
