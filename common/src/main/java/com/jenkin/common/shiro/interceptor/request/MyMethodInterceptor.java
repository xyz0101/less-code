package com.jenkin.common.shiro.interceptor.request;

import com.jenkin.common.shiro.handler.MyAuthHandler;
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
public class MyMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {
    public MyMethodInterceptor( ) {
        super(new MyAuthHandler() );
    }

    public MyMethodInterceptor( AnnotationResolver resolver) {
        super(new MyAuthHandler(),resolver);
    }

    @Override
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        super.assertAuthorized(mi);
    }

}
