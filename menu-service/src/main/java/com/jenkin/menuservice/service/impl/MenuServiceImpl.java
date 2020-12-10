package com.jenkin.menuservice.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.entity.pos.MenuPo;
import com.jenkin.menuservice.dao.MenuMapper;
import com.jenkin.menuservice.service.MenuService;
import org.springframework.stereotype.Service;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuPo> implements MenuService {
}
