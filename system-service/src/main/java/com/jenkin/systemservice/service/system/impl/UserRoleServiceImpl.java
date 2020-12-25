package com.jenkin.systemservice.service.system.impl;

import com.jenkin.common.entity.dtos.system.RoleDto;
import com.jenkin.common.entity.pos.system.RolePo;
import com.jenkin.common.entity.pos.system.UserRolePo;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.shiro.service.impl.BaseUserRoleServiceImpl;

import com.jenkin.common.utils.BeanUtils;
import com.jenkin.systemservice.dao.system.UserRoleMapper;
import com.jenkin.systemservice.service.system.RoleService;
import com.jenkin.systemservice.service.system.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service()
public class UserRoleServiceImpl extends BaseUserRoleServiceImpl<UserRoleMapper,UserRolePo> implements UserRoleService {

    @Autowired
    private RoleService roleService;

    @Override
    public List<UserRolePo> listByUserId(Integer id) {
        if (id==null) {
            return new ArrayList<>();
        }
        MyQueryWrapper<UserRolePo> myQueryWrapper = new MyQueryWrapper<>();
        myQueryWrapper.eq(UserRolePo.Fields.userId,id);
        return list(myQueryWrapper);
    }

    /**
     * 根据用户ID获取用户角色
     *
     * @param userIds
     * @return
     */
    @Override
    public Map<Integer,List<RoleDto> > listByUserIds(List<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new HashMap<>();
        }
        Map<Integer,List<RoleDto> > res = new HashMap<>();
        MyQueryWrapper<UserRolePo> myQueryWrapper = new MyQueryWrapper<>();
        myQueryWrapper.in(UserRolePo.Fields.userId,userIds);
        List<UserRolePo> list = list(myQueryWrapper);
        if (!CollectionUtils.isEmpty(list)) {

            Set<Integer> roleids = list.stream().map(UserRolePo::getRoleId).collect(Collectors.toSet());
            List<RolePo> rolePos =  CollectionUtils.isEmpty(roleids)?new ArrayList<>():roleService.listByIds(roleids);
            List<RoleDto> roleDtos = BeanUtils.mapList(rolePos, RoleDto.class);
            Map<Integer,RoleDto> roleDtoMap = new HashMap<>();
            roleDtos.forEach(item->roleDtoMap.put(item.getId(),item));

            list.forEach(item->{
                List<RoleDto> userRolePos = res.get(item.getUserId());
                userRolePos=userRolePos==null?new ArrayList<>():userRolePos;
                userRolePos.add(roleDtoMap.get(item.getRoleId()));
                res.put(item.getUserId(),userRolePos);
            });
        }
        return res;
    }
}
