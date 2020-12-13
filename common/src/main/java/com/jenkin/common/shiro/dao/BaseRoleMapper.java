package com.jenkin.common.shiro.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenkin.common.entity.pos.system.RolePo;
import org.springframework.context.annotation.Primary;

/**
 * @author jenkin
 * @className RoleMapper
 * @description TODO
 * @date 2020/12/9 15:53
 */
@Primary
public interface BaseRoleMapper extends BaseMapper<RolePo> {
}
