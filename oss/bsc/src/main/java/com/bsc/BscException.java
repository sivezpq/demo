package com.bsc;

/**
 * 封装白山云的异常，使用运行时异常
 * @author: huagang
 * @date: 2023年4月28日
 * @version: 1.0
 * 
 */
public class BscException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    public BscException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public BscException(String message, Throwable cause) {
        super(message, cause);
    }

}
