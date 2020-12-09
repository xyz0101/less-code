package com.jenkin.simpleshiro.shiro.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.jenkin.simpleshiro.shiro.anno.IgnoreCheck;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.spring.aop.SpringAnnotationResolver;
import org.springframework.web.bind.annotation.*;

import static com.jenkin.simpleshiro.shiro.Const.PREFIX;

public class MyAnnotationResolver extends SpringAnnotationResolver {
    public MyAnnotationResolver() {
    }

    public Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz) {
        Method m = mi.getMethod();
        String prefix = "";
        if (m.getDeclaringClass().isAnnotationPresent(RequestMapping.class)) {
            prefix = m.getDeclaringClass().getAnnotation(RequestMapping.class).value()[0];
        }
        PREFIX.set(prefix);
        if(m.isAnnotationPresent(IgnoreCheck.class)){
            return null;
        }
        if (m.isAnnotationPresent(GetMapping.class)){
            return m.getAnnotation(GetMapping.class);
        }else   if (m.isAnnotationPresent(PutMapping.class)){
            return m.getAnnotation(PutMapping.class);
        }else  if (m.isAnnotationPresent(PostMapping.class)){
            return m.getAnnotation(PostMapping.class);
        }else  if (m.isAnnotationPresent(DeleteMapping.class)){
            return m.getAnnotation(DeleteMapping.class);
        }else{
            return m.getAnnotation(RequestMapping.class);
        }
    }
}
