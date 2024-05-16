package com.common.response;

public enum ResultsCode implements IError {
    SUCCESS(200, "成功"),
    PARAMETER_ERROR(401, "参数错误"),
    NOT_LOGIN(402, "未登录"),
    PARAMETER_MISS_ERROR(404, "参数缺失错误"),
    SIGNATURE_ERROR(600, "签名校验失败"),
    AUTHORITY_ERROR(601, "权限校验失败"),
    LIMIT_ERROR(700, "限流"),
    FALLBACK_ERROR(701, "降级"),
    DUPLICATE_KEY_ERROR(800, "数据重复"),
    SYSTEM_EXCEPTION(500, "未知异常");

    private int code;
    private String message;

    private ResultsCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }
    @Override
    public String getMessage() {
        return this.message;
    }
}
