package com.jenkin.menuservice.shiro.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.jenkin.common.anno.MyPermission;
import com.jenkin.common.anno.IgnoreCheck;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.spring.aop.SpringAnnotationResolver;
import org.springframework.web.bind.annotation.*;


public class MyAnnotationResolver extends SpringAnnotationResolver {
    public MyAnnotationResolver() {
    }
    @Override
    public Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz) {
        Method m = mi.getMethod();
        if(m.isAnnotationPresent(IgnoreCheck.class)){
            return null;
        }
        if (m.isAnnotationPresent(MyPermission.class)){
            return m.getAnnotation(GetMapping.class);
        }
        return null;
    }
}
