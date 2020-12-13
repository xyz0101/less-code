package com.jenkin.common.anno;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jenkin
 * @className MyPermission
 * @description TODO
 * @date 2020/12/9 16:07
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyPermission {
    String[] value() ;
    String name() default "";
}
