package com.jenkin.fileservice.wallpaper_server.strategy.impl;

import com.jenkin.common.entity.vos.aibizhi.AbzResponse;
import com.jenkin.common.entity.vos.aibizhi.Category;
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
@Component(WallpaperStrategy.WPS_PREFIX+"RandomStrategy")
@Slf4j
@Data
public class RandomStrategy extends BaseStrategy {

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
    public static final int CURRENT_USER_WALLPAPER_MAX_RETRY_COUNT=50;




    /**
     * 顺序获取壁纸
     * @return
     */
    @Override
    public Wallpaper resolveWallpaper() {

        Redis redis = ApplicationContextProvider.getBean(Redis.class);

        AibizhiService aibizhiService = ApplicationContextProvider.getBean(AibizhiService.class);
        List<String> categories = getCategories();
        if(CollectionUtils.isEmpty(categories)){
            log.info("未配置分类");
            return null;
        }
        int size = categories.size();
        int classIndex = (int) (Math.random() * 100);
        classIndex = classIndex%size;
        String classCode= categories.get(classIndex);
        Category category = aibizhiService.getAibizhiCategory()
                .getRes()
                .getCategory()
                .stream()
                .filter(item -> item.getId().equalsIgnoreCase(classCode))
                .findFirst()
                .orElse(null);
        String retryCountKey = CURRENT_USER_WALLPAPER_RETRY_COUNT_KEY.replace("{userCode}",getUserCode());

        if (category!=null){
            int count = category.getCount();
            count = count/20;
            int index = (int) (Math.random() * (100000+count));
            index = index%count;
//            int skip = index/PAGE_SIZE;
            int i = index % PAGE_SIZE;
//            if (i==0){
//                skip+=1;
//            }
//            skip=skip+ i;
            log.info("准备获取壁纸，分类：{}，，索引： {}",classCode,index);
            AbzResponse<Wallpaper> wallpaper = aibizhiService.getWallpaperWin(classCode, index,"picasso,170,windows");
            List<Wallpaper> wallpaperList = wallpaper.getRes().getWallpaper();
            if(!CollectionUtils.isEmpty(wallpaperList)){
                log.info("获取到随机壁纸：{}",wallpaperList.get(i));
                redis.set(retryCountKey,0);
                return wallpaperList.get(i);
            }else{
                log.info("壁纸列表为空");
            }
        }
        long incr = redis.incr(retryCountKey, 1L);
        if(incr>CURRENT_USER_WALLPAPER_MAX_RETRY_COUNT){
            log.info("随机获取壁纸 失败，超过了最大重试次数 ");
            return null;
        }
        return resolveWallpaper();
    }


    public String getStrategyCode(){
        return "RandomStrategy";
    }
    public String getStrategyName(){
        return "随机策略";
    }
}
