package com.jenkin.systemservice.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenkin.common.entity.pos.system.RolePo;
import com.jenkin.common.shiro.dao.BaseRoleMapper;

/**
 * @author jenkin
 * @className RoleMapper
 * @description TODO
 * @date 2020/12/9 15:53
 */
public interface RoleMapper extends BaseRoleMapper, BaseMapper<RolePo> {
}
