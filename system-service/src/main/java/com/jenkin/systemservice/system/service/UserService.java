package com.jenkin.systemservice.system.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.pos.system.UserPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.UserQo;
import com.jenkin.common.shiro.service.BaseUserService;

/**
 * @author jenkin
 * @className MenuService
 * @description TODO
 * @date 2020/12/9 15:58
 */
public interface UserService extends BaseUserService  , IService<UserPo> {


    /**
     * 分页获取用户
     * @param user
     */
    Page<UserDto> listByPage(BaseQo<UserQo> user);

    /**
     * 保存用户信息
     * @param userDto
     * @return
     */
    UserDto saveUserInfo(UserDto userDto);

    /**
     * 注册新用户
     * @param userDto
     */
    void register(UserDto userDto);
}
