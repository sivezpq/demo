package com.common.exception;

/**
 * 全局异常类
 * @author lxy
 */
public class GlobalException extends RuntimeException {

	private String code;
	private String message;

	public GlobalException(String message) {
		super(message);
		this.message = message;
	}

	public GlobalException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
