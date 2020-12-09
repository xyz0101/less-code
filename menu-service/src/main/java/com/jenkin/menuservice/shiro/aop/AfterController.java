package com.jenkin.menuservice.shiro.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;


/**
 * @author ：jenkin
 * @date ：Created at 2020/12/6 14:11
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class AfterController {

    @Pointcut("execution(* com.jenkin.menuservice.controller.*(..))")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result =null;
        try {
            result = point.proceed();
        }finally {

        }
        return result;
    }


}
