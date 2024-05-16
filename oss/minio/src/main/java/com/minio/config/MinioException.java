package com.minio.config;

/**
 * minio异常类
 * 
 */
public class MinioException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    public MinioException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public MinioException(String message, Throwable cause) {
        super(message, cause);
    }

}
