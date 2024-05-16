package com.retry.aspect;

import com.retry.annotation.RetryAnnotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.BackOffPolicyBuilder;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RetryAspect {

    private static final Logger logger = LoggerFactory.getLogger(RetryAspect.class);

    @Around("@annotation(com.retry.annotation.RetryAnnotation)")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = methodSignature.getMethod();
        RetryAnnotation retryAnnotation = method.getAnnotation(RetryAnnotation.class);

        RetryTemplate retryTemplate = new RetryTemplate();
        BackOffPolicy backOffPolicy = BackOffPolicyBuilder.newBuilder()
                .maxDelay(retryAnnotation.backoff().maxDelay())
                .multiplier(retryAnnotation.backoff().multiplier())
                .delay(retryAnnotation.backoff().delay())
                .build();
        retryTemplate.setBackOffPolicy(backOffPolicy);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(retryAnnotation.maxAttempt());// 重试次数
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate.execute(arg0 -> {
            int retryCount = arg0.getRetryCount();
            logger.info("Sending request method: {}, maxAttempt : {}, delay: {}, retryCount: {}",
                    method.getName(), retryAnnotation.maxAttempt(), retryAnnotation.backoff().delay(), retryCount);
            return joinPoint.proceed(joinPoint.getArgs());
        }, context -> {
            //重试失败后执行的代码
            logger.error("重试了{}次后，还是失败！", context.getRetryCount(), context.getLastThrowable());
            return "failed callback";
        });
    }


}
