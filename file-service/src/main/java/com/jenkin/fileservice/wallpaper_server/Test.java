package com.jenkin.fileservice.wallpaper_server;

import com.alibaba.fastjson.JSON;
import com.jenkin.fileservice.wallpaper_server.strategy.WallpaperStrategy;
import com.jenkin.fileservice.wallpaper_server.strategy.impl.OrderStrategy;

import java.util.ArrayList;

/**
 * @author ：jenkin
 * @date ：Created at 2021/9/26 12:48
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Test {
    public static void main(String[] args) {
        OrderStrategy orderStrategy = new OrderStrategy();
        orderStrategy.setStart(1);
        orderStrategy.setCategories(new ArrayList<>());
        orderStrategy.setTimeGap(2);
        orderStrategy.setTimeUnit(3);

        String s = JSON.toJSONString(orderStrategy);
        WallpaperStrategy instance = WallpaperStrategy.getInstance("", s);
        System.out.println(instance.resolveWallpaper());

    }
}
