package com.jenkin.menuservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.entity.dtos.UserDto;
import com.jenkin.common.entity.pos.UserPo;

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
