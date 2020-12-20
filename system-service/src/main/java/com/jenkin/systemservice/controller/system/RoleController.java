package com.jenkin.systemservice.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.qos.system.RoleQo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.dtos.system.RoleDto;
import com.jenkin.common.entity.pos.system.RolePo;
import com.jenkin.common.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jenkin.systemservice.service.system.RoleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;


/**
 * @author ：jenkin
 * @date ：Created at 2020-12-20 18:21:07
 * @description：
 * @modified By：
 * @version: 1.0
 */
@RestController
@RequestMapping("/system")
@CrossOrigin
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/listAllData")
    @ApiOperation("获取所有信息，不分页")
    public Response< List<RoleDto>> listAllData(){
        List<RolePo> res =  roleService.list();
        return Response.ok(BeanUtils.mapList(res,RoleDto.class));
    }

    @PostMapping("/listByPage")
    @ApiOperation("分页获取信息")
    public Response<Page<RoleDto>> listRoleByPage(@RequestBody BaseQo<RoleQo> qo){
        Page<RoleDto>  roleDtoPage =  roleService.listByPage(qo);
        return Response.ok( roleDtoPage);
    }
    @GetMapping("/getSingleById")
    @ApiOperation("根据id获取信息")
    public Response< RoleDto> getSingleById(Integer id){
        if (id==null) {
            return Response.ok();
        }
        RoleDto byId =  roleService.getById(id);
        return Response.ok(byId);
    }
    @PostMapping("/save")
    @ApiOperation("保存信息")
    public Response save(@RequestBody  RoleDto  roleDto){
        RoleDto data =  roleService.saveRoleInfo( roleDto);
        return Response.ok();
    }


    @PostMapping("/deleteById")
    @ApiOperation("删除")
    public Response deleteRole(@RequestBody Integer[] ids){
        roleService.removeByIds(Arrays.asList(ids));
        return Response.ok();
    }



}
