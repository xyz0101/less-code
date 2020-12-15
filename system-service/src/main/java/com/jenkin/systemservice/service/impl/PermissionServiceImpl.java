package com.jenkin.systemservice.service.impl;

import com.jenkin.common.entity.pos.system.PermissionPo;
import com.jenkin.common.shiro.service.impl.BasePermissionServiceImpl;
import com.jenkin.systemservice.dao.PermissionMapper;
import com.jenkin.systemservice.service.PermissionService;
import org.springframework.stereotype.Service;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service()
public class PermissionServiceImpl extends BasePermissionServiceImpl<PermissionMapper, PermissionPo> implements PermissionService {
}
