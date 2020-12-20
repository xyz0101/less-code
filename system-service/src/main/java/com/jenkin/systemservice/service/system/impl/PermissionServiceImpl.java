package com.jenkin.systemservice.service.system.impl;

import com.jenkin.common.entity.pos.system.PermissionPo;
import com.jenkin.common.shiro.service.impl.BasePermissionServiceImpl;
import com.jenkin.systemservice.dao.system.PermissionMapper;
import com.jenkin.systemservice.service.system.PermissionService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.system.PermissionDto;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.PermissionQo;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.SimpleQuery;
import org.springframework.stereotype.Service;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service()
public class PermissionServiceImpl extends BasePermissionServiceImpl<PermissionMapper, PermissionPo> implements PermissionService {

    @Override
    public PermissionDto getById(Integer id) {
        return BeanUtils.map(getById(id),PermissionDto.class);
    }

    /**
     * 分页获取数据
     *
     * @param  permission
     */
    @Override
    public Page<PermissionDto> listByPage(BaseQo<PermissionQo>  permission) {
        SimpleQuery<PermissionPo> simpleQuery = SimpleQuery.builder( permission,this).sort();
        MyQueryWrapper<PermissionPo> query = simpleQuery.getQuery();
        return simpleQuery.page(PermissionDto.class);
    }

    /**
     * 保存信息
     *
     * @param  permission
     * @return
     */
    @Override
    public PermissionDto savePermissionInfo(PermissionDto  permission) {
        saveOrUpdate( permission);
        return  permission;
    }
}
