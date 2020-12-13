package com.jenkin.common.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.entity.pos.system.PermissionPo;
import com.jenkin.common.shiro.dao.BasePermissionMapper;
import com.jenkin.common.shiro.service.BasePermissionService;
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
public class BasePermissionServiceImpl<M, P> extends ServiceImpl<BasePermissionMapper, PermissionPo> implements BasePermissionService {
}
