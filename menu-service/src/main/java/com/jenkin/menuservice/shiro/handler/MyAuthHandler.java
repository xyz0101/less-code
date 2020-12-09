package com.jenkin.menuservice.shiro.handler;

import com.jenkin.menuservice.anno.MyPermission;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

import java.lang.annotation.Annotation;
import java.util.Arrays;


/**
 * @author ：jenkin
 * @date ：Created at 2020/12/6 11:33
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class MyAuthHandler extends AuthorizingAnnotationHandler {
    public MyAuthHandler() {
        super(MyPermission.class);
    }

    @Override
    public void assertAuthorized(Annotation annotation) throws AuthorizationException {
        if (annotation==null) {
            return;
        }
        System.out.println(annotation.getClass().toGenericString());


            String[] perms = getAnnotationValue(annotation);
            System.out.println("mapping::"+ Arrays.toString(perms));
            System.out.println("权限::"+ Arrays.toString(perms));
            // Avoid processing exceptions unnecessarily - "delay" throwing the exception by calling hasRole first
            boolean hasAtLeastOnePermission = false;
            for (String permission : perms){
                if (getSubject().isPermitted(permission)){
                    hasAtLeastOnePermission = true;
                }

            }
            // Cause the exception if none of the role match, note that the exception message will be a bit misleading
            if (!hasAtLeastOnePermission) {
                getSubject().checkPermission(perms[0]);
            }

    }
    private String[] getAnnotationValue(Annotation a) {

        if (a instanceof MyPermission){
           return ((MyPermission) a).value();
        }
        return new String[0];
    }
}
