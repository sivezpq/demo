package com.common.response;

public final class BaseCodeEntity {
    private int code;
    private String message;

    private BaseCodeEntity(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static BaseCodeEntity of(int code, String message) {
        return new BaseCodeEntity(code, message);
    }
}
