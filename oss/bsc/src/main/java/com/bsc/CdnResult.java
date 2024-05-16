package com.bsc;

/**
 * 白山云CDN结果对象
 * @author: huagang
 * @date: 2023年4月28日
 * @version: 1.0
 */
public class CdnResult<T> {

    private String code;
    private String message;
    private T data;

    public CdnResult() {
    }

    public CdnResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CdnResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}