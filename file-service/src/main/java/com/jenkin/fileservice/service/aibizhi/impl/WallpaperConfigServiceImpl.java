package com.jenkin.fileservice.service.aibizhi.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.file.WallpaperConfigDto;
import com.jenkin.common.entity.pos.file.WallpaperConfigPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.file.WallpaperConfigQo;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.SimpleQuery;
import com.jenkin.fileservice.dao.file.WallpaperConfigMapper;
import com.jenkin.fileservice.service.aibizhi.WallpaperConfigService;
import org.springframework.stereotype.Service;


/**
 * @author ：jenkin
 * @date ：Created at 2021-09-26 19:21:47
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Service
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
        return  wallpaperConfig;
    }

}
