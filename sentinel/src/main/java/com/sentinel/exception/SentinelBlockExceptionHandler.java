package com.sentinel.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.common.response.Response;

@Component
public class SentinelBlockExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        Response<Object> resultObject = null;
        //根据限流降级的策略规则，不同的异常返回不同的提示语，
        if (e instanceof FlowException) {
            resultObject = new Response(10010, "服务限流了");
        }else if (e instanceof DegradeException){
            resultObject = new Response(10020, "服务降级了");
        }else if (e instanceof ParamFlowException){
            resultObject = new Response(10030, "热点参数限流了");
        }else if (e instanceof SystemBlockException){
            resultObject = new Response(10040, "触发系统保护规则了");
        }else if(e instanceof AuthorityException){
            resultObject = new Response(10050, "授权规则不通过");
        }

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), resultObject);

        //如果需要跳转到指定页面使用如下处理：
        //request.getRequestDispatcher("/index.jsp").forward(request,response);
        //跳转到网站
        //response.sendRedirect("https://blog.csdn.net/weixin_44985880");
    }

}
