package com.jenkin.fileservice.wallpaper_server.strategy.impl;

import com.jenkin.common.entity.vos.aibizhi.AbzResponse;
import com.jenkin.common.entity.vos.aibizhi.Wallpaper;
import com.jenkin.common.utils.ApplicationContextProvider;
import com.jenkin.common.utils.Redis;
import com.jenkin.fileservice.service.aibizhi.AibizhiService;
import com.jenkin.fileservice.wallpaper_server.strategy.BaseStrategy;
import com.jenkin.fileservice.wallpaper_server.strategy.WallpaperStrategy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2021/9/26 11:22
 * @description：顺序循环策略
 * @modified By：
 * @version: 1.0
 */
@Component(WallpaperStrategy.WPS_PREFIX+"OrderStrategy")
@Slf4j
@Data
public class OrderStrategy extends BaseStrategy {

    /**
     * 当前用户的壁纸分类索引
     */
    public static final String CURRENT_USER_WALLPAPER_CLASS_INDEX_KEY="wallpaper:current:classIndex:{userCode}";
    /**
     * 用户获取壁纸的重试次数
     */
    public static final String CURRENT_USER_WALLPAPER_RETRY_COUNT_KEY="wallpaper:current:retryCount:{userCode}";
    /**
     * 用户获取壁纸醉的的重试次数
     */
    public static final int CURRENT_USER_WALLPAPER_MAX_RETRY_COUNT=20;



    private Integer start;

    /**
     * 顺序获取壁纸
     * @return
     */
    @Override
    public Wallpaper resolveWallpaper() {

        Redis redis = ApplicationContextProvider.getBean(Redis.class);
        String curIndexKey = CURRENT_USER_WALLPAPER_INDEX_KEY.replace("{userCode}",getUserCode());
        int index = calculateIndex(curIndexKey,redis);
        int i = index % 20;
        int nextI = i+1;
        int page = ((index/20)*20)+ i>0?i:0;
        if(i==0){
            nextI = 0;
        }
        AibizhiService aibizhiService = ApplicationContextProvider.getBean(AibizhiService.class);
        String classIndexKey = CURRENT_USER_WALLPAPER_CLASS_INDEX_KEY.replace("{userCode}",getUserCode());
        Integer classIndex = calculateIndex(classIndexKey, redis);
        List<String> categories = getCategories();
        if(CollectionUtils.isEmpty(categories)){
            log.info("未配置分类");
            return null;
        }
        if(classIndex>categories.size()){
            classIndex=0;
        }
        String classCode = categories.get(classIndex);
        AbzResponse<Wallpaper> wallpaper = aibizhiService.getWallpaperWin(classCode, page,"picasso,170,windows");
        List<Wallpaper> wallpaperList = wallpaper.getRes().getWallpaper();
        if (CollectionUtils.isEmpty(wallpaperList)){
            classIndex=classIndex+1;
        }else{
            Wallpaper pic = wallpaperList.get(nextI);
            redis.set(curIndexKey,nextI);
            redis.set(classIndexKey,classIndex);
            log.info("获取壁纸 {} ",pic);
            return pic;
        }
        redis.set(classIndexKey,classIndex);
        String retryCountKey = CURRENT_USER_WALLPAPER_RETRY_COUNT_KEY.replace("{userCode}",getUserCode());
        long incr = redis.incr(retryCountKey, 1L);
        if(incr>CURRENT_USER_WALLPAPER_MAX_RETRY_COUNT){
            log.info("顺序获取壁纸 失败，超过了最大重试次数 ");
            return null;
        }
        return resolveWallpaper();
    }

    private Integer calculateIndex(String key, Redis redis) {
        Object o = redis.get(key);
        if (o==null){
            o="0";
        }
       return Integer.parseInt(o.toString());
    }

    public String getStrategyCode(){
        return "OrderStrategy";
    }

}
