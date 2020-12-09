package com.jenkin.menuservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.menuservice.entity.dtos.UserDto;
import com.jenkin.menuservice.entity.pos.MenuPo;
import com.jenkin.menuservice.entity.pos.UserPo;

/**
 * @author jenkin
 * @className MenuService
 * @description TODO
 * @date 2020/12/9 15:58
 */
public interface UserService extends IService<UserPo> {
    /**
     * 根据用户编号获取用户
     * @param code
     * @return
     */
    UserDto getByCode(String code);
}
