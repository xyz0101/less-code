package com.jenkin.common.aspects;

import com.jenkin.common.anno.EnableErrorCatch;
import com.jenkin.common.anno.IgnoreErrorCatch;
import com.jenkin.common.entity.Response;
import com.jenkin.common.exception.LscException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(value=1)
public class ErrorCatchAspect {


    @Pointcut(value = "execution(public * com.jenkin.*.*.controller..*.*(..))")
    public void controllerPoint(){ }

    @Around("controllerPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取签名
        Signature signature= joinPoint.getSignature();
        //将签名转为方法签名
        MethodSignature methodSignature = (MethodSignature) signature;
        //获取方法
        Method method = methodSignature.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        EnableErrorCatch anno = null;
        if (declaringClass.isAnnotationPresent(EnableErrorCatch.class)) {
            anno = declaringClass.getDeclaredAnnotation(EnableErrorCatch.class);
        }else{
            anno=method.isAnnotationPresent(EnableErrorCatch.class)?method.getDeclaredAnnotation(EnableErrorCatch.class):null;
        }
        if (method.isAnnotationPresent(IgnoreErrorCatch.class)) {
            anno=null;
        }

        Object res = null;
        try {
            res = joinPoint.proceed();
        }catch (Throwable e){
            e.printStackTrace();
            if (anno==null) {
                throw e;
            }
            LscException exception;
            if (e.getClass().isAssignableFrom(LscException.class)) {
                exception = (LscException) e;
            }else{
                exception = LscException.systemException(e.getMessage());
            }
            String code = exception.getType().getCode();
            String msg = exception.getMsg();
            res = Response.error(code,msg);
        }
        return res;
    }

}
