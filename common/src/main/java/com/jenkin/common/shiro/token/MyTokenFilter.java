package com.jenkin.common.shiro.token;

import com.alibaba.fastjson.JSON;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.shiro.AESUtil;
import com.jenkin.common.utils.Redis;
import com.jenkin.common.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

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
                if(token==null){
                    token = httpServletRequest.getParameter("token");
                    token = new String(Base64.getDecoder().decode(token));
                    log.info("当前的url token：{}", token);
                }
                log.info("当前的token：{}", token);

                if (token != null) {
                    UserDto userDto = JSON.parseObject(AESUtil.decrypt(token), UserDto.class);
                    String key = TOKEN_KEY + userDto.getUserCode();
                    Object o = redis.get(key);
                    if (o == null||!((MyToken)o).getToken().equals(token)) {
                        redirectToLogin(request, response);
                        return false;
                    } else {
                        getSubject(request,response).login((MyToken)o);
                        redis.set(key, o, EXPIRE_TIME);
                        return true;
                    }
                } else {
                    redirectToLogin(request, response);
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    redirectToLogin(request, response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return false;
            }
    }
}
