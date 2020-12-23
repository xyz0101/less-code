package com.jenkin.common.shiro.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.system.RoleDto;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.entity.pos.system.RolePo;
import com.jenkin.common.entity.pos.system.UserPo;
import com.jenkin.common.entity.pos.system.UserRolePo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.UserQo;
import com.jenkin.common.shiro.dao.BaseUserMapper;
import com.jenkin.common.shiro.service.BaseMenuService;
import com.jenkin.common.shiro.service.BaseRoleService;
import com.jenkin.common.shiro.service.BaseUserRoleService;
import com.jenkin.common.shiro.service.BaseUserService;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.ShiroUtils;
import com.jenkin.common.utils.SimpleQuery;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service
@Primary
public class BaseUserServiceImpl<M, P> extends ServiceImpl<BaseUserMapper, UserPo> implements BaseUserService {
    @Autowired
    BaseUserRoleService baseUserRoleService;
    @Autowired
    BaseRoleService baseRoleService;
    @Autowired
    BaseMenuService baseMenuService;
    @Override
    public UserDto getByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            return null;
        }
        MyQueryWrapper<UserPo> myQueryWrapper = new MyQueryWrapper<>();
        UserPo userPo = getOne(myQueryWrapper.eq(UserPo.Fields.userCode, code));
        return BeanUtils.map(userPo,UserDto.class);
    }

    /**
     * 获取用户的详细信息
     *
     * @param code
     * @return
     */
    @Override
    public UserDto getCurrentUserInfo(String code) {
        UserDto userDto =  getByCode(code);
        if (userDto==null) {
            return null;
        }
        List<UserRolePo> allByUserId = baseUserRoleService.listByUserId(userDto.getId() );
        List<Integer> collect = allByUserId.stream().map(UserRolePo::getRoleId).collect(Collectors.toList());
        List<RolePo> allById = CollectionUtils.isEmpty(collect)?new ArrayList<>():baseRoleService.listByIds(collect);
        List<RoleDto> roleDtos = new ArrayList<>(allById.size());
        if (!CollectionUtils.isEmpty(allById)) {
            for (RolePo role : allById) {
                RoleDto roleDto = BeanUtils.map(role,RoleDto.class);
                if (StringUtils.hasLength(role.getMenuStr())) {
                    List<Integer> ids= new ArrayList<>();
                    for (String s : role.getMenuStr().split(",")) {
                        ids.add(Integer.parseInt(s));
                    }
                    List<MenuPo> allMenu = CollectionUtils.isEmpty(ids)?new ArrayList<>()
                            :baseMenuService.listByIds(ids);
                    roleDto.setMenus(allMenu);
                    roleDtos.add(roleDto);
                }
            }
        }
        userDto.setRoles(roleDtos);
        return userDto;
    }


}
