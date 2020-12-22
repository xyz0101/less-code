package com.jenkin.systemservice.service.system.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.pos.system.UserPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.UserQo;
import com.jenkin.common.shiro.service.impl.BaseUserServiceImpl;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.ShiroUtils;
import com.jenkin.common.utils.SimpleQuery;
import com.jenkin.systemservice.dao.system.UserMapper;
import com.jenkin.systemservice.service.system.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
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
        UserQo data = user.getData();
        SimpleQuery<UserPo> simpleQuery = SimpleQuery.builder(user,this).sort();
        MyQueryWrapper<UserPo> query = simpleQuery.getQuery();
        if (data!=null) {
            query.like(StringUtils.isNotEmpty(data.getUserCode()),UserPo.Fields.userCode,data.getUserCode());
            query.like(StringUtils.isNotEmpty(data.getUserName()),UserPo.Fields.userName,data.getUserName());
            query.like(StringUtils.isNotEmpty(data.getUserEmail()),UserPo.Fields.userEmail,data.getUserEmail());

        }
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
        if (StringUtils.isNotEmpty(user.getPassword())&&user.getId()==null) {
            String salt = RandomStringUtils.randomAlphanumeric(20);
            user.setSalt(salt);
            user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
        }
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
