package com.common.response;

import java.io.Serializable;

public class Response<T> implements Serializable {
    private static final long serialVersionUID = -9087500797904937497L;
    private int code;
    private String message;
    private T data;

    public Response() {
    }

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(IError error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public static <T> Response<T> success() {
        return (new Response()).setCode(ResultsCode.SUCCESS.getCode()).setMessage(ResultsCode.SUCCESS.getMessage());
    }

    public static <T> Response<T> success(T data) {
        return (new Response()).setCode(ResultsCode.SUCCESS.getCode()).setMessage(ResultsCode.SUCCESS.getMessage()).setData(data);
    }

    public static <T> Response<T> fail(int code, String message) {
        return (new Response()).setCode(code).setMessage(message);
    }

    public static <T> Response<T> fail(int code, String message, T data) {
        return (new Response()).setCode(code).setMessage(message).setData(data);
    }

    public static <T> Response<T> fail(IError error, T data) {
        return (new Response()).setCode(error.getCode()).setMessage(error.getMessage()).setData(data);
    }

    public static <T> Response<T> fail(ResultsCode resultCode) {
        return (new Response()).setCode(resultCode.getCode()).setMessage(resultCode.getMessage());
    }

    public static <T> Response<T> fail(BaseCodeEntity baseCodeEntity) {
        return (new Response()).setCode(baseCodeEntity.getCode()).setMessage(baseCodeEntity.getMessage());
    }

    public static <T> Response<T> fail(IError error) {
        return (new Response()).setCode(error.getCode()).setMessage(error.getMessage());
    }

    public static <T> Response<T> fail(ResultsCode resultCode, T data) {
        return (new Response()).setCode(resultCode.getCode()).setMessage(resultCode.getMessage()).setData(data);
    }

    public static <T> Response<T> fail(BaseCodeEntity baseCodeEntity, T data) {
        return (new Response()).setCode(baseCodeEntity.getCode()).setMessage(baseCodeEntity.getMessage()).setData(data);
    }

    public static <T> Response<T> exception(ResultsCode resultCode, Throwable throwable) {
        Response<T> result = new Response();
        result.setCode(resultCode.getCode());
        result.setMessage(throwable.getClass().getName() + ", " + throwable.getMessage());
        return result;
    }

    public static <T> Response<T> exception(BaseCodeEntity baseCodeEntity, Throwable throwable) {
        Response<T> result = new Response();
        result.setCode(baseCodeEntity.getCode());
        result.setMessage(throwable.getClass().getName() + ", " + throwable.getMessage());
        return result;
    }

    public int getCode() {
        return this.code;
    }

    public Response<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }
}
