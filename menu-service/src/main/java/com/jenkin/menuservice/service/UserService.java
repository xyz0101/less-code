package com.jenkin.menuservice.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.pos.system.UserPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.UserQo;

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
