package com.jenkin.menuservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.entity.pos.system.RolePo;
import com.jenkin.menuservice.dao.RoleMapper;
import com.jenkin.menuservice.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RolePo> implements RoleService {
}
