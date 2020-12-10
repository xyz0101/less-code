package com.jenkin.menuservice.shiro;

import com.jenkin.common.anno.MyPermission;
import com.jenkin.common.anno.IgnoreCheck;

import com.jenkin.menuservice.shiro.interceptor.request.RequestMappingAopInterceptor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/6 11:52
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class ShiroAdvisor extends AuthorizationAttributeSourceAdvisor {
    public ShiroAdvisor() {
        setAdvice(new RequestMappingAopInterceptor());
    }

    @Override
    public boolean matches(Method method, Class targetClass) {
        if (targetClass != null) {
            try {
                method = targetClass.getMethod(method.getName(), method.getParameterTypes());
                return this.isFrameAnnotation(method);
            } catch (NoSuchMethodException ignored) {

            }
        }

        return super.matches(method, targetClass);
    }
    private boolean isFrameAnnotation(Method method) {
        return (null != AnnotationUtils.findAnnotation(method, MyPermission.class)

                )&&
                null==AnnotationUtils.findAnnotation(method,IgnoreCheck.class);
    }

}
