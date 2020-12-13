package com.jenkin.common.shiro.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.pos.system.UserPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.UserQo;
import com.jenkin.common.shiro.dao.BaseUserMapper;
import com.jenkin.common.shiro.service.BaseUserService;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.ShiroUtils;
import com.jenkin.common.utils.SimpleQuery;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service
@Primary
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, UserPo> implements BaseUserService {
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
