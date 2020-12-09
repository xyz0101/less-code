package com.jenkin.simpleshiro.shiro.handler;

import com.jenkin.simpleshiro.shiro.interceptor.annovalue.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import static com.jenkin.simpleshiro.shiro.Const.PREFIX;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/6 11:33
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class RequestMappingAuthHandler extends AuthorizingAnnotationHandler {
    public RequestMappingAuthHandler() {
        super(RequestMapping.class);
    }

    @Override
    public void assertAuthorized(Annotation annotation) throws AuthorizationException {
        if (annotation==null) {
            return;
        }
        System.out.println(annotation.getClass().toGenericString());


            String[] perms = getAnnotationValue(annotation);
            System.out.println("mapping::"+ Arrays.toString(perms));
            String s = PREFIX.get();
            for (int i = 0; i < perms.length; i++) {
                perms[i] = s+perms[i];
                if (perms[i].startsWith("/")){
                    perms[i] = perms[i].substring(1);
                }
                perms[i]= perms[i].replaceAll("/",":");
            }
            System.out.println("权限::"+ Arrays.toString(perms));
            // Avoid processing exceptions unnecessarily - "delay" throwing the exception by calling hasRole first
            boolean hasAtLeastOnePermission = false;
            for (String permission : perms) if (getSubject().isPermitted(permission)) hasAtLeastOnePermission = true;
            // Cause the exception if none of the role match, note that the exception message will be a bit misleading
            if (!hasAtLeastOnePermission) getSubject().checkPermission(perms[0]);

    }
    private String[] getAnnotationValue(Annotation a) {
        AnnoValueGetter annoValueGetter = null;
        if (a instanceof GetMapping){
            annoValueGetter = new GetMappingValueGetter();
        }else  if (a instanceof PutMapping){
            annoValueGetter = new PutMappingValueGetter();
        }else  if (a instanceof DeleteMapping){
            annoValueGetter = new DeleteMappingValueGetter();
        }else  if (a instanceof PostMapping){
            annoValueGetter = new PostMappingValueGetter();
        }else{
            annoValueGetter = new RequestMappingValueGetter();
        }
        return annoValueGetter.getAnnoValue(a);
    }
}
