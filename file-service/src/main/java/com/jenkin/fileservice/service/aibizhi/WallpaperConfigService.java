package com.jenkin.fileservice.service.aibizhi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.entity.dtos.file.WallpaperConfigDto;
import com.jenkin.common.entity.pos.file.WallpaperConfigPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.file.WallpaperConfigQo;
import com.jenkin.common.entity.vos.aibizhi.Wallpaper;

/**
 * @author ：jenkin
 * @date ：Created at 2021-09-26 19:21:47
 * @description：
 * @modified By：
 * @version: 1.0
 */
public interface WallpaperConfigService extends IService<WallpaperConfigPo> {

        /**
         * 根据ID编号获取数据
         * @param id
         * @return
         */
        WallpaperConfigDto getById(Integer id);

        /**
         * 分页获取数据
         * @param  wallpaperConfig
         */
        Page<WallpaperConfigDto> listByPage(BaseQo<WallpaperConfigQo> wallpaperConfig);

        /**
         * 保存信息
         * @param  wallpaperConfigDto
         * @return
         */
        WallpaperConfigDto saveWallpaperConfigInfo(WallpaperConfigDto wallpaperConfigDto);

        /**
         * 获取用户的配置
         * @param userCode
         * @return
         */
        WallpaperConfigDto getConfigByUser(String userCode);

        /**
         * 校验用户并且获取用户配置
         * @param userCode
         * @param password
         * @return
         */
        WallpaperConfigDto getConfigByUserWithAuth(String userCode, String password);

        /**
         * 修改壁纸
         * @param wallpaper
         */
        void changeWallpaper(Wallpaper wallpaper);
}
