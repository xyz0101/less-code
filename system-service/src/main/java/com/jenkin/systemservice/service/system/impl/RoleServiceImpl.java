package com.jenkin.systemservice.service.system.impl;

import com.jenkin.common.entity.pos.system.RolePo;
import com.jenkin.common.shiro.service.impl.BaseRoleServiceImpl;
import com.jenkin.systemservice.dao.system.RoleMapper;
import com.jenkin.systemservice.service.system.RoleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.system.RoleDto;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.RoleQo;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.SimpleQuery;

/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service()
public class RoleServiceImpl extends BaseRoleServiceImpl<RoleMapper, RolePo> implements RoleService {
    @Override
    public RoleDto getById(Integer id) {
        return BeanUtils.map(getById(id),RoleDto.class);
    }

    /**
     * 分页获取数据
     *
     * @param  role
     */
    @Override
    public Page<RoleDto> listByPage(BaseQo<RoleQo>  role) {
        SimpleQuery<RolePo> simpleQuery = SimpleQuery.builder( role,this).sort();
        MyQueryWrapper<RolePo> query = simpleQuery.getQuery();
        return simpleQuery.page(RoleDto.class);
    }

    /**
     * 保存信息
     *
     * @param  role
     * @return
     */
    @Override
    public RoleDto saveRoleInfo(RoleDto  role) {
        saveOrUpdate( role);
        return  role;
    }

}
