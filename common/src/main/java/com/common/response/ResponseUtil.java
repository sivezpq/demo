package com.common.response;

/**
 * Created by IntelliJ IDEA
 * User: yanxin.
 * Date: 2022/5/26.
 * Time: 10:48
 * To change this template use File Or Preferences | Settings | Editor | File and Code Templates.
 * File Description: 生成Response工具类
 */
public class ResponseUtil {

    private ResponseUtil(){

    }

    //根据ResultEnum生成Response
    public static Response<Object> generateResponse(ResultEnum resultEnum) {
        return new Response<>(resultEnum.getCode(), resultEnum.getMessage());
    }

    //根据ResultEnum和data生成Response
    public static Response<Object> generateResponse(ResultEnum resultEnum, Object data) {
        return new Response<>(resultEnum.getCode(), resultEnum.getMessage(), data);
    }
}
