package com.jenkin.common.shiro.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenkin.common.entity.pos.system.UserPo;
import org.springframework.context.annotation.Primary;

/**
 * @author jenkin
 * @className MenuPo
 * @description TODO
 * @date 2020/12/9 15:52
 */
@Primary
public interface BaseUserMapper extends BaseMapper<UserPo> {
}
