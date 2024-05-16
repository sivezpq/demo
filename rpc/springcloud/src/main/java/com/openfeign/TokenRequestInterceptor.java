package com.openfeign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 获取请求中的消息头
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.isNotBlank(requestHeader) && requestHeader.startsWith("Bearer ")) {
            // 将消息头塞入到请求模板中
            requestTemplate.header(HttpHeaders.AUTHORIZATION, requestHeader);
        }
        //重写URL，访问指定服务
        String newUrl = "http://localhost:8082";
        requestTemplate.target(newUrl);
    }
}