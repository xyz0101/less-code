package com.jenkin.common.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.shiro.dao.BaseMenuMapper;
import com.jenkin.common.shiro.service.BaseMenuService;
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
public class BaseMenuServiceImpl<M, P> extends ServiceImpl<BaseMenuMapper, MenuPo> implements BaseMenuService {
}
