package com.jenkin.common.shiro.handler;

import com.jenkin.common.entity.Response;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "com.jenkin")
@ResponseBody
public class MyGlobalExceptionHandler {


    @ExceptionHandler(IncorrectCredentialsException.class)
    public Response customException(Exception e) {
        e.printStackTrace();
        return Response.error("用户名或密码错误");
    }
    @ExceptionHandler(AuthorizationException.class)
    public Response authException(Exception e) {
        e.printStackTrace();
        return Response.error("无权限访问");
    }
    @ExceptionHandler(AuthenticationException.class)
    public Response loginException(Exception e) {
        e.printStackTrace();
        return Response.error("登录失效，请重新登录");
    }



}
