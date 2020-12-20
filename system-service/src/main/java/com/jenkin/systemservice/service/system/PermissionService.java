package com.jenkin.systemservice.service.system;


import com.jenkin.common.shiro.service.BasePermissionService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.entity.dtos.system.PermissionDto;
import com.jenkin.common.entity.pos.system.PermissionPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.PermissionQo;

/**
 * @author jenkin
 * @className MenuService
 * @description TODO
 * @date 2020/12/9 15:58
 */
public interface PermissionService extends BasePermissionService, IService<PermissionPo> {
    /**
     * 根据ID编号获取数据
     * @param id
     * @return
     */
    PermissionDto getById(Integer id);

    /**
     * 分页获取数据
     * @param  permission
     */
    Page<PermissionDto> listByPage(BaseQo<PermissionQo>  permission);

    /**
     * 保存信息
     * @param  permissionDto
     * @return
     */
    PermissionDto savePermissionInfo(PermissionDto  permissionDto);
}
