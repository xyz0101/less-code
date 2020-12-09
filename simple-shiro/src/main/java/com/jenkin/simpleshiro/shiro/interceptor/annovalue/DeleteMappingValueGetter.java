package com.jenkin.simpleshiro.shiro.interceptor.annovalue;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.lang.annotation.Annotation;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/6 21:13
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Component
public class DeleteMappingValueGetter implements AnnoValueGetter{
    @Override
    public String[] getAnnoValue(Annotation a) {
        System.out.println("delete");
        DeleteMapping anno = (DeleteMapping) a;
        return anno.value();
    }
}
