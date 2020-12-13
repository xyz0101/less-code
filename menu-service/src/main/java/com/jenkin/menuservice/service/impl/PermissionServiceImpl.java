package com.jenkin.menuservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.entity.pos.system.PermissionPo;
import com.jenkin.common.entity.pos.system.UserPo;
import com.jenkin.common.shiro.service.impl.BasePermissionServiceImpl;
import com.jenkin.menuservice.dao.PermissionMapper;
import com.jenkin.menuservice.service.PermissionService;
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
