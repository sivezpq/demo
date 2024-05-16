package com.website.framework.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class GlobalAop {

    @Around("@within(org.springframework.stereotype.Controller)")
//    @Around("execution(public * *(..))")
    public Object aroundAop(final ProceedingJoinPoint pjp) throws Throwable {
        try {
            Object[] args = pjp.getArgs();
            Object obj = pjp.proceed();
            System.out.println("return:"+obj);
            return obj;
        } catch(Throwable ex) {
            throw ex;
        }
    }
}
