package com.jenkin.systemservice.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.entity.dtos.system.RoleDto;
import com.jenkin.common.entity.pos.system.UserRolePo;
import com.jenkin.common.shiro.service.BaseUserRoleService;

import java.util.List;
import java.util.Map;

/**
 * @author jenkin
 * @className MenuService
 * @description TODO
 * @date 2020/12/9 15:58
 */
public interface UserRoleService extends BaseUserRoleService , IService<UserRolePo> {
    /**
     * 根据用户ID获取用户角色关系
     * @param id
     * @return
     */
    List<UserRolePo> listByUserId(Integer id);

    /**
     * 根据用户ID获取用户角色关系
     * @param userIds
     * @return
     */
    Map<Integer,List<RoleDto> > listByUserIds(List<Integer> userIds);
}
