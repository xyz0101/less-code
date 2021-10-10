package com.jenkin.fileservice.wallpaper_server.strategy;

import com.jenkin.common.entity.vos.aibizhi.Wallpaper;
import lombok.Data;

import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2021/9/26 11:14
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
public  class BaseStrategy implements WallpaperStrategy{

    /**
     * 当前用户的壁纸的URL
     */
    public static final String CURRENT_USER_WALLPAPER_URL_KEY="wallpaper:current:url:{userCode}";
    /**
     * 壁纸的分页大小
     */
    public static final Integer PAGE_SIZE = 20;
    /**
     * 是否启用
     */
    private boolean onFlag;

    /**
     * 策略编码
     */
    private String strategyCode;
    /**
     * 策略名称
     */
    private String strategyName;

    /**
     * 用户编码
     */
    private String userCode;
    /**
     * 分类列表
     */
    private List<String> categories;
    /**\
     * 时间间隔
     */
    private Integer timeGap;
    /**
     * 时间间隔单位
     * @see com.jenkin.common.enums.TimeUnitEnum
     */
    private Integer timeUnit;

    @Override
    public Wallpaper resolveWallpaper() {
        throw new UnsupportedOperationException();
    }
}
