package com.jenkin.systemservice.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.pos.system.UserPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.UserQo;
import com.jenkin.common.shiro.service.impl.BaseUserServiceImpl;
import com.jenkin.common.utils.ShiroUtils;
import com.jenkin.common.utils.SimpleQuery;
import com.jenkin.systemservice.dao.UserMapper;
import com.jenkin.systemservice.service.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service()
public class UserServiceImpl extends BaseUserServiceImpl<UserMapper,UserPo> implements UserService {


    /**
     * 分页获取用户
     *
     * @param user
     */
    @Override
    public Page<UserDto> listByPage(BaseQo<UserQo> user) {
        SimpleQuery<UserPo> simpleQuery = SimpleQuery.builder(user,this).sort();
        MyQueryWrapper<UserPo> query = simpleQuery.getQuery();
        return simpleQuery.page(UserDto.class);
    }

    /**
     * 保存用户信息
     *
     * @param user
     * @return
     */
    @Override
    public UserDto saveUserInfo(UserDto user) {
        //sha256加密
//        String salt = RandomStringUtils.randomAlphanumeric(20);
//        user.setSalt(salt);
//        user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));

        saveOrUpdate(user);
        return user;
    }

    /**
     * 注册新用户
     *
     * @param userDto
     */
    @Override
    public void register(UserDto userDto) {
        if(checkCheckCode(userDto.getCheckCode())){
            saveUserInfo(userDto);
        }
    }

    /**
     * 校验邮箱编码
     * @param checkCode
     * @return
     */
    private boolean checkCheckCode(String checkCode) {
        // TODO
        return true;
    }
}
