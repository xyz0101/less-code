package com.jenkin.simpleshiro.shiro.interceptor.request;

import com.jenkin.simpleshiro.shiro.interceptor.MyAnnotationResolver;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/6 11:45
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class RequestMappingAopInterceptor extends AopAllianceAnnotationsAuthorizingMethodInterceptor {


    public RequestMappingAopInterceptor() {
        super();
        this.methodInterceptors.add(new RequestMappingMethodInterceptor(new MyAnnotationResolver()));
    }

}
