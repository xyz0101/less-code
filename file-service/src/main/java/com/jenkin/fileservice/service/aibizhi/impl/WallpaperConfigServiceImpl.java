package com.jenkin.fileservice.service.aibizhi.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.file.WallpaperConfigDto;
import com.jenkin.common.entity.pos.file.WallpaperConfigPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.file.WallpaperConfigQo;
import com.jenkin.common.entity.vos.aibizhi.Wallpaper;
import com.jenkin.common.exception.ExceptionEnum;
import com.jenkin.common.exception.LscException;
import com.jenkin.common.shiro.utils.ShiroUtils;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.SimpleQuery;
import com.jenkin.fileservice.dao.file.WallpaperConfigMapper;
import com.jenkin.fileservice.service.aibizhi.WallpaperConfigService;
import com.jenkin.fileservice.wallpaper_server.WallpaperServerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author ：jenkin
 * @date ：Created at 2021-09-26 19:21:47
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Service
@Slf4j
public class WallpaperConfigServiceImpl extends ServiceImpl<WallpaperConfigMapper, WallpaperConfigPo> implements WallpaperConfigService {

    @Override
    public WallpaperConfigDto getById(Integer id) {
        return BeanUtils.map(getById(id),WallpaperConfigDto.class);
    }

    /**
     * 分页获取数据
     *
     * @param  wallpaperConfig
     */
    @Override
    public Page<WallpaperConfigDto> listByPage(BaseQo<WallpaperConfigQo>  wallpaperConfig) {
        SimpleQuery<WallpaperConfigPo> simpleQuery = SimpleQuery.builder( wallpaperConfig,this).sort();
        MyQueryWrapper<WallpaperConfigPo> query = simpleQuery.getQuery();
        return simpleQuery.page(WallpaperConfigDto.class);
    }

    /**
     * 保存信息
     *
     * @param  wallpaperConfig
     * @return
     */
    @Override
    public WallpaperConfigDto saveWallpaperConfigInfo(WallpaperConfigDto  wallpaperConfig) {
        saveOrUpdate( wallpaperConfig);
        if (!WallpaperServerHelper.sendUpdateConfigMsg(ShiroUtils.getUserCode(), wallpaperConfig.getStrategyValue())) {
            log.warn("{}  客户端没有启动",ShiroUtils.getUserCode());
        }
        return  wallpaperConfig;
    }

    /**
     * 获取用户配置
     * @param userCode
     * @param password
     * @return
     */
    @Override
    public WallpaperConfigDto getConfigByUserWithAuth(String userCode, String password) {

       return getConfigByUser(userCode);
    }
    @Override
    public void changeWallpaper(Wallpaper wallpaper){
        if (!WallpaperServerHelper.sendChangeWallpaperMsg(ShiroUtils.getUserCode(),wallpaper)){
            throw new LscException(ExceptionEnum.CLIANT_OFFLINE_EXCEPTION);
        }

    }

    /**
     * 获取用户的配置
     *
     * @param userCode
     * @return
     */
    @Override
    public WallpaperConfigDto getConfigByUser(String userCode) {
        MyQueryWrapper<WallpaperConfigPo> queryWrapper = new MyQueryWrapper<>();
        queryWrapper.eq(WallpaperConfigPo.Fields.userCode,userCode);
        WallpaperConfigPo configPo = getOne(queryWrapper);
        return BeanUtils.map(configPo,WallpaperConfigDto.class);
    }
}
