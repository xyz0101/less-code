package com.jenkin.systemservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.entity.pos.system.RolePo;
import com.jenkin.common.shiro.service.BaseRoleService;

/**
 * @author jenkin
 * @className MenuService
 * @description TODO
 * @date 2020/12/9 15:58
 */
public interface RoleService extends BaseRoleService , IService<RolePo> {
}
