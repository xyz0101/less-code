package com.jenkin.common.config;

import com.jenkin.common.constant.HistroyConst;
import com.jenkin.common.utils.ApplicationContextProvider;
import com.jenkin.common.utils.Redis;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

import static com.jenkin.common.constant.ThreadPoolConst.THREAD_HEADER;

/**
* @author jenkin
* @version 1.0
* @desc feign 设置请求头
* @date 2020年12月28日20:47:10
*/
@Configuration
public class FeignRequestConfig implements RequestInterceptor {
   @Override
   public void apply(RequestTemplate template) {
       ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

       if (attributes!=null) {
           HttpServletRequest request = attributes.getRequest();
           Enumeration<String> headerNames = request.getHeaderNames();

           String requestURI = request.getRequestURI();
           if(requestURI!=null&&requestURI.endsWith("getAbzWallpaperWin")){
               template.header("User-Agent", "picasso,170,windows");
           }
           if(requestURI==null||requestURI.contains("history")){
              String authorization= request.getHeader("authorization");
               Redis redis = ApplicationContextProvider.getBean("redis", Redis.class);
               String uid = String.valueOf(redis.get(HistroyConst.USER_TASK_TOKEN_UID+request.getHeader("token")));
               String tkey= HistroyConst.USER_TASK_TOKEN+uid;
               Object o = redis.get(tkey);
               if (o != null) {
                   authorization= String.valueOf(o);
               }
               template.header("authorization", authorization);
           }
           template.header("token", request.getHeader("token"));

           if (headerNames == null) {
               return;
           }
       }else if (THREAD_HEADER.get()!=null){
           Map<String, String> map = THREAD_HEADER.get();
           template.header("authorization",map.get("authorization"));
       }

   }
}

