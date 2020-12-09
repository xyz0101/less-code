package com.jenkin.simpleshiro.shiro.interceptor.annovalue;

import org.springframework.web.bind.annotation.PutMapping;

import java.lang.annotation.Annotation;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/6 21:13
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class PutMappingValueGetter implements AnnoValueGetter{
    @Override
    public String[] getAnnoValue(Annotation a) {
        System.out.println("put");
        PutMapping anno = (PutMapping) a;
        return anno.value();
    }
}
