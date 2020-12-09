package com.jenkin.simpleshiro.shiro.interceptor.request;

import com.jenkin.simpleshiro.shiro.handler.RequestMappingAuthHandler;
import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/6 11:37
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class RequestMappingMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {
    public RequestMappingMethodInterceptor( ) {
        super(new RequestMappingAuthHandler() );
    }

    public RequestMappingMethodInterceptor( AnnotationResolver resolver) {
        super(new RequestMappingAuthHandler(),resolver);
    }

    @Override
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        super.assertAuthorized(mi);
    }

}
