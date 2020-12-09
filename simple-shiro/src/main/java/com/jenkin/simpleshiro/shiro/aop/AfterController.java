package com.jenkin.simpleshiro.shiro.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

import static com.jenkin.simpleshiro.shiro.Const.PREFIX;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/6 14:11
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class AfterController {

    @Pointcut("execution(* com.jenkin.simpleshiro.controller.*(..))")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result =null;
        try {
            result = point.proceed();
        }finally {
            PREFIX.remove();
        }
        return result;
    }


}
