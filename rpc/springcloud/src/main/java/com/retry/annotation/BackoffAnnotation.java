package com.retry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BackoffAnnotation {
    long maxDelay() default 0L;

    long delay() default 1000L;

    double multiplier() default 0.0D;
}
