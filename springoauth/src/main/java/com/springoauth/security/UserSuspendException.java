package com.springoauth.security;

import org.springframework.security.core.AuthenticationException;

public class UserSuspendException extends AuthenticationException {
    public UserSuspendException(String msg) {
        super(msg);
    }

    public UserSuspendException(String msg, Throwable cause) {
        super(msg, cause);
    }
}