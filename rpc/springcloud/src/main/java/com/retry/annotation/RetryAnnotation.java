package com.retry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryAnnotation {
    int maxAttempt() default 3;

    BackoffAnnotation backoff() default @BackoffAnnotation();

    Class<? extends Throwable>[] include() default {};
}
