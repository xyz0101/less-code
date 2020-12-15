package com.jenkin.common.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.pos.system.UserRolePo;
import com.jenkin.common.shiro.dao.BaseUserRoleMapper;
import com.jenkin.common.shiro.service.BaseUserRoleService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service
@Primary
public class BaseUserRoleServiceImpl<M, P> extends ServiceImpl<BaseUserRoleMapper, UserRolePo> implements BaseUserRoleService {
    @Override
    public List<UserRolePo> listByUserId(Integer id) {
        if (id==null) {
            return new ArrayList<>();
        }
        MyQueryWrapper<UserRolePo> myQueryWrapper = new MyQueryWrapper<>();
        myQueryWrapper.eq(UserRolePo.Fields.userId,id);
        return list(myQueryWrapper);
    }
}
