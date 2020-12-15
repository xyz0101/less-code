package com.jenkin.common.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.entity.pos.system.RolePo;
import com.jenkin.common.shiro.dao.BaseRoleMapper;
import com.jenkin.common.shiro.service.BaseRoleService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service
@Primary
public class BaseRoleServiceImpl<M, P> extends ServiceImpl<BaseRoleMapper, RolePo> implements BaseRoleService {
}
