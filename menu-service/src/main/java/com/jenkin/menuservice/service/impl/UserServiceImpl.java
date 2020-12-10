package com.jenkin.menuservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.UserDto;
import com.jenkin.common.entity.pos.UserPo;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.menuservice.dao.UserMapper;
import com.jenkin.menuservice.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements UserService {
    @Override
    public UserDto getByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            return null;
        }
        MyQueryWrapper<UserPo> myQueryWrapper = new MyQueryWrapper<>();
        UserPo userPo = getOne(myQueryWrapper.eq(UserPo.Fields.userCode, code));
        return BeanUtils.map(userPo,UserDto.class);
    }
}
