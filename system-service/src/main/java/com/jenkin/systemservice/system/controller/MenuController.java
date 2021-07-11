package com.jenkin.systemservice.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.config.JoinBuilder;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.pos.BasePo;
import com.jenkin.common.entity.pos.system.*;
import com.jenkin.common.entity.qos.system.MenuQo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.dtos.system.MenuDto;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.shiro.utils.ShiroUtils;
import com.jenkin.systemservice.system.service.MenuService;
import com.jenkin.systemservice.system.service.UserRoleService;
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
    public Response<List<MenuDto>> listAllData() {
        List<MenuPo> res = menuService.list();
        return Response.ok(BeanUtils.mapList(res, MenuDto.class));
    }


    @GetMapping("/listMenuTree")
    @ApiOperation("获取菜单树形结构")
    public Response<List<MenuDto>> listMenuTree() {
        List<MenuDto> res = menuService.listMenuTree();
        return Response.ok(BeanUtils.mapList(res, MenuDto.class));
    }

    @PostMapping("/listByPage")
    @ApiOperation("分页获取信息")
    public Response<Page<MenuDto>> listMenuByPage(@RequestBody BaseQo<MenuQo> qo) {
        Page<MenuDto> menuDtoPage = menuService.listByPage(qo);
        return Response.ok(menuDtoPage);
    }

    @GetMapping("/getSingleById")
    @ApiOperation("根据id获取信息")
    public Response<MenuDto> getSingleById(Integer id) {
        if (id == null) {
            return Response.ok();
        }
        MenuDto byCode = menuService.getById(id);
        return Response.ok(byCode);
    }

    @PostMapping("/save")
    @ApiOperation("保存信息")
    public Response save(@RequestBody MenuDto menuDto) {
        MenuDto data = menuService.saveMenuInfo(menuDto);
        return Response.ok();
    }


    @PostMapping("/deleteById")
    @ApiOperation("删除")
    public Response deleteMenu(@RequestBody Integer[] ids) {
        menuService.deleteMenuByIds(Arrays.asList(ids));
        return Response.ok();
    }


    @GetMapping("/listMenuByUser")
    @ApiOperation("获取当前用户有权限的菜单")
    public Response<List<MenuDto>> listMenuByUser() {
        String userCode = ShiroUtils.getUserCode();
        List<MenuDto> res = menuService.listMenuByUser(userCode);
        return Response.ok(res);
    }


    @GetMapping("/getMaxOrder")
    @ApiOperation("获取当前菜单下面最大的序号")
    public Response<Integer> getMaxOrder(Integer menuId) {
        List<MenuDto> menuDtos = menuService.listByParentId(menuId);
        return Response.ok(menuDtos.size() + 1);
    }

    @GetMapping("/test")
    public Response test() {

        MyQueryWrapper<MenuPo> queryWrapper = new MyQueryWrapper<>();
        //添加一个关联表查询，关联用户表
        queryWrapper.addJoin(
                JoinBuilder.build()
                        //查询用户表里面的用户名称和用户邮箱字段
                        .selectField(MenuPo.Fields.userName, MenuPo.Fields.userEmail)
                        //使用左连接关联
                        .join(JoinBuilder.LEFT, MenuPo.class, UserPo.class)
                        //设置关联条件
                        .on(JoinBuilder.CaseBuilder.build()
                                //主表的创建人字段等于关联表的用户编码字段
                                // 注意，在条件中默认是第一个参数为主表的字段，第二个参数为关联表的字段
                                .eq(BasePo.Fields.createdBy, UserPo.Fields.userCode)
                        )
        //再添加一个关联查询，关联角色表
        ).addJoin(
                JoinBuilder.build()
                        //查血角色表里面的角色名称
                        .selectField(MenuPo.Fields.roleName)
                        //左连接
                        .join(JoinBuilder.LEFT,MenuPo.class, RolePo.class)
                        //关联条件
                        .on(JoinBuilder.CaseBuilder.build()
                                //code等于角色code
                            .eq(MenuPo.Fields.code, RolePo.Fields.roleCode)
                                //并且
                            .and()
                                //括号
                            .brackets(
                                    //parent =-1 or parent =1
                                    JoinBuilder.CaseBuilder.build()
                                            .eq(MenuPo.Fields.parent,-1)
                                            .or()
                                            .eq(MenuPo.Fields.parent,1)
                            )
                )
         //外层筛选条件，用户名=jenkin
        ).eq(MenuPo.Fields.userName,"jenkin");
        //执行查询
        menuService.list(queryWrapper);
        return Response.ok();
    }

}
