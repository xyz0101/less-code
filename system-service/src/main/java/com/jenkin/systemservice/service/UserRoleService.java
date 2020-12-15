package com.jenkin.systemservice.service;

import com.jenkin.common.entity.pos.system.UserRolePo;
import com.jenkin.common.shiro.service.BaseUserRoleService;

import java.util.List;

/**
 * @author jenkin
 * @className MenuService
 * @description TODO
 * @date 2020/12/9 15:58
 */
public interface UserRoleService extends BaseUserRoleService {
    /**
     * 根据用户ID获取用户角色关系
     * @param id
     * @return
     */
    List<UserRolePo> listByUserId(Integer id);
}
