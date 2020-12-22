package com.jenkin.systemservice.service.system.impl;

import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.entity.pos.system.PermissionPo;
import com.jenkin.common.exception.ExceptionEnum;
import com.jenkin.common.exception.LscException;
import com.jenkin.common.shiro.service.impl.BaseMenuServiceImpl;
import com.jenkin.systemservice.dao.system.MenuMapper;
import com.jenkin.systemservice.service.system.MenuService;
import com.jenkin.systemservice.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.system.MenuDto;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.MenuQo;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.SimpleQuery;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author jenkin
 * @className MenuServiceImpl
 * @description TODO
 * @date 2020/12/9 15:58
 */
@Service()
public class MenuServiceImpl extends BaseMenuServiceImpl<MenuMapper, MenuPo> implements MenuService {

    @Autowired
    private PermissionService permissionService;

    public static final int MENU_ROOT_PARENT_ID=-1;


    @Override
    public MenuDto getById(Integer id) {
        MenuPo byId = super.getById(id);
        return BeanUtils.map(byId,MenuDto.class);
    }

    /**
     * 分页获取数据
     *
     * @param  menu
     */
    @Override
    public Page<MenuDto> listByPage(BaseQo<MenuQo>  menu) {
        SimpleQuery<MenuPo> simpleQuery = SimpleQuery.builder( menu,this).sort();
        MyQueryWrapper<MenuPo> query = simpleQuery.getQuery();
        return simpleQuery.page(MenuDto.class);
    }

    /**
     * 保存信息
     *
     * @param  menu
     * @return
     */
    @Override
    public MenuDto saveMenuInfo(MenuDto  menu) {
        Integer parent = menu.getParent();
        int level = 0;
        if (parent>0) {
            MenuDto byId = getById(parent);
            if (byId!=null) {
                level = byId.getMenuLevel()+1;
            }
        }
       menu.setMenuLevel(level);
        saveOrUpdate( menu);
        return  menu;
    }

    /**
     * 获取菜单的树形结构
     *
     * @return
     */
    @Override
    public List<MenuDto> listMenuTree() {

        List<MenuPo> list = list();
        Map<Integer,List<Integer>> idsMap = new HashMap<>();
        list.stream().forEach(item ->  {
            if (!StringUtils.isEmpty(item.getPermissions())) {
                idsMap.put(item.getId(),Arrays.stream(item.getPermissions().split(","))
                        .map(s->Integer.parseInt(s)).
                        collect(Collectors.toList()));

            }
        }) ;

        List<Integer> ids = new ArrayList<>();
        for (List<Integer> id : idsMap.values()) {
            ids.addAll(id);
        }
        List<PermissionPo> permissionPos = CollectionUtils.isEmpty(ids)?new ArrayList<>(): permissionService.listByIds(ids);


        List<MenuDto> menuDtos = BeanUtils.mapList(list, MenuDto.class);
        for (MenuDto menuDto : menuDtos) {
            List<Integer> perIds = idsMap.get(menuDto.getId());
            menuDto.setPermissionPos(permissionPos.stream().filter(item->perIds.contains(item.getId())).collect(Collectors.toList()));
        }
        List<MenuDto> parents =  menuDtos.stream()
                .filter(item->item.getParent().equals(MENU_ROOT_PARENT_ID)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(parents)) {
            for (MenuDto parent : parents) {
                convertToTree(parent,menuDtos);
            }
        }
        return parents;
    }

    private void convertToTree(MenuDto menuDto, List<MenuDto> menuDtos) {
        List<MenuDto> parents =  menuDtos.stream()
                .filter(item->item.getParent().equals(menuDto.getId())).collect(Collectors.toList());
        menuDto.setSubList(parents);
        if (!CollectionUtils.isEmpty(parents)) {
            for (MenuDto parent : parents) {
                convertToTree(parent,menuDtos);
            }


        }
    }

    /**
     * 根据ID删除 数据
     *
     * @param ids
     */
    @Override
    public void deleteMenuByIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new LscException(ExceptionEnum.DELETE_TOOMUCH_EXCEPTION);
        }

        List<MenuDto> menuDtos = this.listMenuTree();
        List<MenuDto> deleteMenus = new ArrayList<>();
        for (MenuDto menuDto : menuDtos) {
            findNeedDeleteId(ids,menuDto,deleteMenus,false);
        }
        Set<Integer> deleteIds = deleteMenus.stream().map(item -> item.getId()).collect(Collectors.toSet());
        removeByIds(deleteIds);



    }

    /**
     * 找出需要被删除的ID的集合
     * @param ids 待删除ID
     * @param menuDto
     * @param deleteMenus
     * @param allSub 是否子节点全部要被删除，如果删除的是一个父节点，那么子节点全部要被删除
     */
    private void findNeedDeleteId(List<Integer> ids,  MenuDto  menuDto,List<MenuDto> deleteMenus,boolean allSub ) {

        if (ids.contains(menuDto.getId())||allSub) {
            deleteMenus.add(menuDto );
            allSub=true;
        }
        if (!CollectionUtils.isEmpty(menuDto.getSubList())) {
            for (MenuDto dto : menuDto.getSubList()) {
                findNeedDeleteId(ids,dto,deleteMenus,allSub);
            }
        }





    }
}
