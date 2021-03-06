package com.jenkin.common.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.entity.pos.system.UserRolePo;


import java.util.List;

/**
 * @author jenkin
 * @className MenuService
 * @description TODO
 * @date 2020/12/9 15:58
 */
public interface BaseUserRoleService extends IService<UserRolePo> {
    /**
     * 根据用户ID获取用户角色关系
     * @param id
     * @return
     */
    List<UserRolePo> listByUserId(Integer id);
}
