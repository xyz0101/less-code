package com.jenkin.systemservice.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.entity.pos.system.RolePo;
import com.jenkin.common.shiro.service.BaseRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.entity.dtos.system.RoleDto;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.RoleQo;
/**
 * @author jenkin
 * @className MenuService
 * @description TODO
 * @date 2020/12/9 15:58
 */
public interface RoleService extends BaseRoleService , IService<RolePo> {
    /**
     * 根据ID编号获取数据
     * @param id
     * @return
     */
    RoleDto getById(Integer id);

    /**
     * 分页获取数据
     * @param  role
     */
    Page<RoleDto> listByPage(BaseQo<RoleQo>  role);

    /**
     * 保存信息
     * @param  roleDto
     * @return
     */
    RoleDto saveRoleInfo(RoleDto  roleDto);

}
