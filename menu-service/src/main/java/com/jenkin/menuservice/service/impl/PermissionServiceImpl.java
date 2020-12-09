package com.jenkin.menuservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.menuservice.dao.MenuMapper;
import com.jenkin.menuservice.dao.PermissionMapper;
import com.jenkin.menuservice.entity.pos.MenuPo;
import com.jenkin.menuservice.entity.pos.PermissionPo;
import com.jenkin.menuservice.service.MenuService;
import com.jenkin.menuservice.service.PermissionService;
import org.springframework.stereotype.Service;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionPo> implements PermissionService {
}
