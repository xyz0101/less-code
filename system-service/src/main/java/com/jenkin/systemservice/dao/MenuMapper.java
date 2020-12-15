package com.jenkin.systemservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.shiro.dao.BaseMenuMapper;

/**
 * @author jenkin
 * @className MenuPo
 * @description TODO
 * @date 2020/12/9 15:52
 */
public interface MenuMapper extends BaseMenuMapper , BaseMapper<MenuPo> {
}
