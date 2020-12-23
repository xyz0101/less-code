package com.jenkin.systemservice.controller.system;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.qos.system.MenuQo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.dtos.system.MenuDto;
import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.ShiroUtils;
import com.jenkin.systemservice.service.system.MenuService;
import com.jenkin.systemservice.service.system.RoleService;
import com.jenkin.systemservice.service.system.UserRoleService;
import com.jenkin.systemservice.service.system.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * @author ：jenkin
 * @date ：Created at 2020-12-20 17:59:30
 * @description：
 * @modified By：
 * @version: 1.0
 */
@RestController
@RequestMapping("/menu")
@CrossOrigin
@Api(tags = "菜单相关接口")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/listAllData")
    @ApiOperation("获取所有信息，不分页")
    public Response< List<MenuDto>> listAllData(){
        List<MenuPo> res =  menuService.list();
        return Response.ok(BeanUtils.mapList(res,MenuDto.class));
    }



    @GetMapping("/listMenuTree")
    @ApiOperation("获取菜单树形结构")
    public Response< List<MenuDto>> listMenuTree(){
        List<MenuDto> res =  menuService.listMenuTree();
        return Response.ok(BeanUtils.mapList(res,MenuDto.class));
    }

    @PostMapping("/listByPage")
    @ApiOperation("分页获取信息")
    public Response<Page<MenuDto>> listMenuByPage(@RequestBody BaseQo<MenuQo> qo){
        Page<MenuDto>  menuDtoPage =  menuService.listByPage(qo);
        return Response.ok( menuDtoPage);
    }
    @GetMapping("/getSingleById")
    @ApiOperation("根据id获取信息")
    public Response< MenuDto> getSingleById(Integer id){
        if (id==null) {
            return Response.ok();
        }
        MenuDto byCode =  menuService.getById(id);
        return Response.ok(byCode);
    }
    @PostMapping("/save")
    @ApiOperation("保存信息")
    public Response save(@RequestBody  MenuDto  menuDto){
        MenuDto data =  menuService.saveMenuInfo( menuDto);
        return Response.ok();
    }


    @PostMapping("/deleteById")
    @ApiOperation("删除")
    public Response deleteMenu(@RequestBody Integer[] ids){
        menuService.deleteMenuByIds(Arrays.asList(ids));
        return Response.ok();
    }


    @GetMapping("/listMenuByUser")
    @ApiOperation("获取当前用户有权限的菜单")
    public Response<List<MenuDto>> listMenuByUser(){
        String userCode = ShiroUtils.getUserCode();
        List<MenuDto> res = menuService.listMenuByUser(userCode);
        return Response.ok(res);
    }




    @GetMapping("/getMaxOrder")
    @ApiOperation("获取当前菜单下面最大的序号")
    public Response<Integer> getMaxOrder(Integer menuId){
        List<MenuDto> menuDtos = menuService.listByParentId(menuId);
        return Response.ok(menuDtos.size()+1);
    }



}
