package com.jenkin.systemservice.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenkin.common.entity.pos.system.UserPo;
import com.jenkin.common.shiro.dao.BaseUserMapper;

/**
 * @author jenkin
 * @className MenuPo
 * @description TODO
 * @date 2020/12/9 15:52
 */
public interface UserMapper extends BaseUserMapper , BaseMapper<UserPo> {
}
