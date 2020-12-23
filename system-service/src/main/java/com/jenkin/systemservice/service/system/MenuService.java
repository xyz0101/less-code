package com.jenkin.systemservice.service.system;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.entity.dtos.system.MenuDto;
import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.MenuQo;
import com.jenkin.common.shiro.service.BaseMenuService;

import java.util.List;
import java.util.Map;

/**
 * @author jenkin
 * @className MenuService
 * @description TODO
 * @date 2020/12/9 15:58
 */
public interface MenuService extends BaseMenuService, IService<MenuPo> {

    /**
     * 根据ID编号获取数据
     * @param id
     * @return
     */
    MenuDto getById(Integer id);

    /**
     * 分页获取数据
     * @param  menu
     */
    Page<MenuDto> listByPage(BaseQo<MenuQo>  menu);

    /**
     * 保存信息
     * @param  menuDto
     * @return
     */
    MenuDto saveMenuInfo(MenuDto  menuDto);

    /**
     * 获取菜单的树形结构
     * @return
     */
    List<MenuDto> listMenuTree();

    /**
     * 获取所有的菜单
     * @return
     */
    List<MenuDto> listMenus();

    /**
     * 获取菜单的map关系
     * @return
     */
    Map<Integer,MenuDto> getMenuMap();

    /**
     * 根据ID删除 数据
     * @param ids
     */
    void deleteMenuByIds(List<Integer> ids);

    /**
     * 根据用户获取菜单
     * @param userCode
     * @return
     */
    List<MenuDto> listMenuByUser(String userCode);

    /**
     * 获取当前父节点下面的子节点
     * @param parent
     * @return
     */
    List<MenuDto> listByParentId(Integer parent);
}
