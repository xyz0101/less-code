package com.jenkin.simpleshiro.shiro.interceptor.annovalue;

import java.lang.annotation.Annotation;

public interface AnnoValueGetter {
    String[] getAnnoValue(Annotation a);
}
