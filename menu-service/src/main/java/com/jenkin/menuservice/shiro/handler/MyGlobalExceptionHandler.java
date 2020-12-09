package com.jenkin.menuservice.shiro.handler;

import com.jenkin.menuservice.entity.Response;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class MyGlobalExceptionHandler {


    @ExceptionHandler(IncorrectCredentialsException.class)
    public Response customException(Exception e) {
        e.printStackTrace();
        return Response.error("用户名或密码错误");
    }
    @ExceptionHandler(AuthorizationException.class)
    public Response aurhException(Exception e) {
        e.printStackTrace();
        return Response.error("无权限访问");
    }



}
