package com.jenkin.systemservice.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.system.RoleDto;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.pos.BasePo;
import com.jenkin.common.entity.pos.system.UserPo;
import com.jenkin.common.entity.pos.system.UserRolePo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.UserQo;
import com.jenkin.common.shiro.service.impl.BaseUserServiceImpl;
import com.jenkin.common.shiro.utils.ShiroUtils;
import com.jenkin.common.utils.SimpleQuery;
import com.jenkin.systemservice.system.dao.UserMapper;
import com.jenkin.systemservice.system.service.UserRoleService;
import com.jenkin.systemservice.system.service.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service()
public class UserServiceImpl extends BaseUserServiceImpl<UserMapper,UserPo> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 分页获取用户
     *
     * @param user
     */
    @Override
    public Page<UserDto> listByPage(BaseQo<UserQo> user) {
        UserQo data = user.getData();
        SimpleQuery<UserPo> simpleQuery = SimpleQuery.builder(user,this).sort();
        MyQueryWrapper<UserPo> query = simpleQuery.getQuery();
        if (data!=null) {
            query.like(StringUtils.isNotEmpty(data.getUserCode()),UserPo.Fields.userCode,data.getUserCode());
            query.like(StringUtils.isNotEmpty(data.getUserName()),UserPo.Fields.userName,data.getUserName());
            query.like(StringUtils.isNotEmpty(data.getUserEmail()),UserPo.Fields.userEmail,data.getUserEmail());

        }
        Page<UserDto> page = simpleQuery.page(UserDto.class);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<Integer> collect = page.getRecords().stream().map(BasePo::getId).collect(Collectors.toList());
            Map<Integer, List<RoleDto>> rolesMap =
                    userRoleService.listByUserIds(collect);
            for (UserDto record : page.getRecords()) {
                record.setRoles(rolesMap.get(record.getId()));
            }

        }
        return page;
    }

    /**
     * 保存用户信息
     *
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto saveUserInfo(UserDto user) {
        //sha256加密
        if (StringUtils.isNotEmpty(user.getPassword())&&user.getId()==null) {
            String salt = RandomStringUtils.randomAlphanumeric(20);
            user.setSalt(salt);
            user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
        }
        saveOrUpdate(user);
        //修改角色关系
        updateRole(user);

        return user;
    }

    private void updateRole(UserDto user) {
        if (user.getId()!=null) {
            MyQueryWrapper<UserRolePo> queryWrapper = new MyQueryWrapper<>();
            queryWrapper.eq(UserRolePo.Fields.userId,user.getId());
            userRoleService.remove(queryWrapper);
            List<UserRolePo> userRolePos = new ArrayList<>();
            for (RoleDto role : user.getRoles()) {
                UserRolePo userRolePo = new UserRolePo();
                userRolePo.setRoleId(role.getId());
                userRolePo.setUserId(user.getId());
                userRolePos.add(userRolePo);
            }
            userRoleService.saveBatch(userRolePos);
        }


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
