package com.common.exception;

import com.common.response.Response;
import com.common.response.ResultEnum;
import com.common.response.ResponseUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA
 * User: yanxin.
 * Date: 2022/5/20.
 * Time: 11:05
 * To change this template use File Or Preferences | Settings | Editor | File and Code Templates.
 * File Description: 全局异常处理
 */
//@Slf4j
//@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 可以捕捉到未定义的一些异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Response<Object> handleException(Exception e, HttpServletRequest request) {
        Response<Object> response = ResponseUtil.generateResponse(ResultEnum.FAILURE);
        response.setMessage(ResultEnum.FAILURE.getMessage() + " " + e.getMessage());
        return response;
    }

}
