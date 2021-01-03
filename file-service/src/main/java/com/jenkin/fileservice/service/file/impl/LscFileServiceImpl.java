package com.jenkin.fileservice.service.file.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.file.LscFileDto;
import com.jenkin.common.entity.pos.file.LscFilePo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.file.LscFileQo;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.SimpleQuery;
import com.jenkin.fileservice.dao.file.LscFileMapper;
import com.jenkin.fileservice.service.file.LscFileService;
import org.springframework.stereotype.Service;


/**
 * @author ：jenkin
 * @date ：Created at 2021-01-03 20:14:19
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Service
public class LscFileServiceImpl extends ServiceImpl<LscFileMapper, LscFilePo> implements LscFileService {

    @Override
    public LscFileDto getById(Integer id) {
        return BeanUtils.map(getById(id),LscFileDto.class);
    }

    /**
     * 分页获取数据
     *
     * @param  lscFile
     */
    @Override
    public Page<LscFileDto> listByPage(BaseQo<LscFileQo>  lscFile) {
        SimpleQuery<LscFilePo> simpleQuery = SimpleQuery.builder( lscFile,this).sort();
        MyQueryWrapper<LscFilePo> query = simpleQuery.getQuery();
        return simpleQuery.page(LscFileDto.class);
    }

    /**
     * 保存信息
     *
     * @param  lscFile
     * @return
     */
    @Override
    public LscFileDto saveLscFileInfo(LscFileDto  lscFile) {
        saveOrUpdate( lscFile);
        return  lscFile;
    }

}
