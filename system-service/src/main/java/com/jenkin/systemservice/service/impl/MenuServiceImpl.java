package com.jenkin.systemservice.service.impl;

import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.shiro.service.impl.BaseMenuServiceImpl;
import com.jenkin.systemservice.dao.MenuMapper;
import com.jenkin.systemservice.service.MenuService;
import org.springframework.stereotype.Service;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service()
public class MenuServiceImpl extends BaseMenuServiceImpl<MenuMapper, MenuPo> implements MenuService {
}
