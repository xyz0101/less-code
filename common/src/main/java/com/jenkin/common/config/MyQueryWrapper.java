package com.jenkin.common.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jenkin.common.entity.qos.Sort;

import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2020/3/13 12:07
 * @description：条件构造器，重写字符串转换方法
 * @modified By：
 * @version: 1.0
 */
public class MyQueryWrapper<T> extends QueryWrapper<T> {
    /**
     * 获取 columnName
     *
     * @param column
     */
    @Override
    protected String columnToString(String column) {
        return StringUtils.camelToUnderline(column);
    }


    public static<T> MyQueryWrapper<T> query(){
        return new MyQueryWrapper<T>();
    }

    /**
     * 排序
     * @param sorts
     * @return
     */
    public QueryWrapper<T> sort(List<Sort> sorts){
        if(!CollectionUtils.isEmpty(sorts)){
            sorts.forEach(item->{
                orderBy(item.getSortField()!=null,"asc".equals(item.getSortValue()),item.getSortField());
            });
        }
        return this;
    }

}
