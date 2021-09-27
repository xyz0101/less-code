package com.jenkin.fileservice.wallpaper_server.strategy;

import com.alibaba.fastjson.JSONObject;
import com.jenkin.common.entity.vos.aibizhi.Wallpaper;
import com.jenkin.common.utils.ApplicationContextProvider;
import com.jenkin.fileservice.wallpaper_server.strategy.impl.OrderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jenkin
 * @date 2021年9月26日11:30:11
 */

public interface WallpaperStrategy {

    String WPS_PREFIX= "wallpaper_strategy";

    static WallpaperStrategy getInstance(String code,String jsonText){
        JSONObject jsonObject = JSONObject.parseObject(jsonText);
        Object strategy = ApplicationContextProvider.getBean(WPS_PREFIX + code);
        List<Field> declaredFields = getFields(strategy);
        Map<String, Field> fieldMap = declaredFields.stream().collect(Collectors.toMap(Field::getName, item -> item));
        System.out.println(fieldMap);
        jsonObject.keySet().forEach(item->{
            Field field = fieldMap.get(item);
            if (field !=null) {
                field.setAccessible(true);
                try {
                    field.set(strategy,jsonObject.get(item));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("字段为空："+item);
            }
        });


       return (WallpaperStrategy) strategy;
    }

    static List<Field> getFields(Object strategy) {
        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass =strategy.getClass();
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
       return fieldList;
    }


    Wallpaper resolveWallpaper();

}
