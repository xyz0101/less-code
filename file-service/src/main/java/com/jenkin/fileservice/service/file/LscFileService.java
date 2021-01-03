package com.jenkin.fileservice.service.file;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.entity.dtos.file.LscFileDto;
import com.jenkin.common.entity.pos.file.LscFilePo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.file.LscFileQo;

/**
 * @author ：jenkin
 * @date ：Created at 2021-01-03 19:41:08
 * @description：
 * @modified By：
 * @version: 1.0
 */
public interface LscFileService extends IService<LscFilePo> {

        /**
         * 根据ID编号获取数据
         * @param id
         * @return
         */
        LscFileDto getById(Integer id);

        /**
         * 分页获取数据
         * @param  lscFile
         */
        Page<LscFileDto> listByPage(BaseQo<LscFileQo> lscFile);

        /**
         * 保存信息
         * @param  lscFileDto
         * @return
         */
        LscFileDto saveLscFileInfo(LscFileDto lscFileDto);
    
}
