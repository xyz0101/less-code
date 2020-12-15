package com.jenkin.systemservice.service.impl;

import com.jenkin.common.entity.pos.system.RolePo;
import com.jenkin.common.shiro.service.impl.BaseRoleServiceImpl;
import com.jenkin.systemservice.dao.RoleMapper;
import com.jenkin.systemservice.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service()
public class RoleServiceImpl extends BaseRoleServiceImpl<RoleMapper, RolePo> implements RoleService {
}
