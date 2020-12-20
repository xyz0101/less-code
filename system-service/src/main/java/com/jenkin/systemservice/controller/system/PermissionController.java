package com.jenkin.systemservice.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.qos.system.PermissionQo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.dtos.system.PermissionDto;
import com.jenkin.common.entity.pos.system.PermissionPo;
import com.jenkin.common.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jenkin.systemservice.service.system.PermissionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * @author ：jenkin
 * @date ：Created at 2020-12-20 18:26:24
 * @description：
 * @modified By：
 * @version: 1.0
 */
@RestController
@RequestMapping("/system")
@CrossOrigin
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/listAllData")
    @ApiOperation("获取所有信息，不分页")
    public Response< List<PermissionDto>> listAllData(){
        List<PermissionPo> res =  permissionService.list();
        return Response.ok(BeanUtils.mapList(res,PermissionDto.class));
    }

    @PostMapping("/listByPage")
    @ApiOperation("分页获取信息")
    public Response<Page<PermissionDto>> listPermissionByPage(@RequestBody BaseQo<PermissionQo> qo){
        Page<PermissionDto>  permissionDtoPage =  permissionService.listByPage(qo);
        return Response.ok( permissionDtoPage);
    }
    @GetMapping("/getSingleById")
    @ApiOperation("根据id获取信息")
    public Response< PermissionDto> getSingleById(Integer id){
        if (id==null) {
            return Response.ok();
        }
        PermissionDto byId =  permissionService.getById(id);
        return Response.ok(byId);
    }
    @PostMapping("/save")
    @ApiOperation("保存信息")
    public Response save(@RequestBody  PermissionDto  permissionDto){
        PermissionDto data =  permissionService.savePermissionInfo( permissionDto);
        return Response.ok();
    }


    @PostMapping("/deleteById")
    @ApiOperation("删除")
    public Response deletePermission(@RequestBody Integer[] ids){
        permissionService.removeByIds(Arrays.asList(ids));
        return Response.ok();
    }



}
