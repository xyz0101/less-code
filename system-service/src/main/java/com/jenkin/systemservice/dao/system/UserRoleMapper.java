package com.jenkin.systemservice.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenkin.common.entity.pos.system.UserRolePo;
import com.jenkin.common.shiro.dao.BaseUserRoleMapper;


/**
 * @author jenkin
 * @className MenuPo
 * @description TODO
 * @date 2020/12/9 15:52
 */
public interface UserRoleMapper extends BaseUserRoleMapper , BaseMapper<UserRolePo> {
}
