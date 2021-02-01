package com.jenkin.common.shiro.token;

import com.alibaba.fastjson.JSON;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.shiro.AESUtil;
import com.jenkin.common.utils.Redis;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;


/**
 * @author ：jenkin
 * @date ：Created at 2020/12/13 17:00
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Slf4j
public class MyTokenFilter extends BasicHttpAuthenticationFilter {
    public static final String TOKEN_KEY="mytoken:shiro:user:";
    public static final int EXPIRE_TIME =24*3600;
    private Redis redis;
    public MyTokenFilter(){

    }
    public MyTokenFilter(Redis redis){
        this.redis = redis;
    }
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            try {
                String token = httpServletRequest.getHeader("token");
                log.info("当前的token：{}", token);
                if(token==null){
                    token = httpServletRequest.getParameter("token");
                    token = new String(Base64.getDecoder().decode(token));
                    log.info("当前的url token：{}", token);
                }
                UserDto userDto = JSON.parseObject(AESUtil.decrypt(token), UserDto.class);
                String key = TOKEN_KEY + userDto.getUserCode();
                Object o = redis.get(key);
                if (o == null||!((MyToken)o).getToken().equals(token)) {
                    return false;
                } else {
                    getSubject(request,response).login((MyToken)o);
                    redis.set(key, o, EXPIRE_TIME);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        servletResponse.setContentType("application/json;charset=utf-8");
        // 设置状态码
        servletResponse.setStatus(HttpStatus.OK.value());
        servletResponse.getWriter().write("{\"data\":null,\"msg\":\"请登录\",\"responseCode\":\"401\"}");
        return false;
    }
}
