package com.common.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回状态码
 * User: zhaoyinhu.
 * Date: 2022/05/11
 */
public enum ResultEnum implements Serializable {
    /**
     * 200 操作成功
     */
    SUCCESS(200, "success"),
    /**
     * 500 服务器内部错误
     */
    FAILURE(500, "服务器内部错误!"),
    /**
     * 400 请求无效!
     */
    INVALID_REQUEST(400, "请求无效!"),
    /**
     * 401 未授权!
     */
    UNAUTHORIZED(401, "未授权!"),
    /**
     * 403 拒绝访问!
     */
    ACCESS_DENIED(403, "拒绝访问!"),

    INVALID_INPUT(80601, "参数输入不合法!"),
    NOT_FOUNT(80206, "未找到记录!"),
    HEADER_ERROR(80103, "请求header参数不对，不可用或无效或未传入!"),

    ALREADY_TRIAL(80104, "已经试用过！"),
    NOT_IN_SUPPORT_CITY(80105, "不在支持的城市列表范围内！");



    /**
     * 响应码
     */
    private int code;
    /**
     * 响应信息
     */
    private String message;


    /**
     * 构造参数
     *
     * @param code    响应码
     * @param message 响应信息
     */
    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取响应码
     *
     * @return code 响应码
     */
    public int getCode() {
        return this.code;
    }


    /**
     * 获取响应信息
     *
     * @return message 响应信息
     */
    public String getMessage() {
        return this.message;
    }


    private static Map<Integer, ResultEnum> zyMap = new HashMap<>();

    static {
        for (ResultEnum value : ResultEnum.values()) {
            zyMap.put(value.getCode(), value);
        }
    }

}