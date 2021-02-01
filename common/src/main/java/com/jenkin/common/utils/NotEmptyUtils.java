package com.jenkin.common.utils;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @author jenkin
 * @className NotEmptyUtils
 * @description TODO
 * @date 2020/6/28 11:16
 */
public class NotEmptyUtils {
    /**
     * 判断对象是否不为空
     * @param val
     * @return
     */
    public static boolean notEmpty(Object val){
        if (val == null) {
            return false;
        }else {
            if (val instanceof String) {
                return !StringUtils.isEmpty(val);
            } else if (val instanceof Collection) {
                return !CollectionUtils.isEmpty((Collection<?>) val);
            } else if (val.getClass().isArray()) {
                return !ArrayUtil.isEmpty(val);
            }else{
                return true;
            }
        }
    }

    /**
     * 判断所有对象是否都不为空
     * @param vals
     * @return
     */
    public static boolean allNotEmpty(Object... vals){
        boolean res = false;
        if (!ArrayUtil.isEmpty(vals)) {
           res = true;
            for (Object val : vals) {
                res=res && notEmpty(val);
            }
        }
        return res;
    }
}
