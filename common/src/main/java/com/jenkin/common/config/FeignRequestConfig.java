package com.jenkin.common.config;

import com.jenkin.common.utils.ShiroUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

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
       HttpServletRequest request = attributes.getRequest();
       Enumeration<String> headerNames = request.getHeaderNames();
       String requestURI = request.getRequestURI();
       template.header("User-Agent", "picasso,170,windows");
       template.header("token", request.getHeader("token"));

       if (headerNames == null) {
           return;
       }
//       //处理上游请求头信息，传递时继续携带
//       while (headerNames.hasMoreElements()) {
//           String name = headerNames.nextElement();
//           String values = request.getHeader(name);
//           template.header(name, values);
//       }
   }
}

