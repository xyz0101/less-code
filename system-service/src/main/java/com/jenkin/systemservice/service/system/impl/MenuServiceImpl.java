package com.jenkin.systemservice.service.system.impl;

import com.jenkin.common.entity.dtos.system.RoleDto;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.entity.pos.system.PermissionPo;
import com.jenkin.common.entity.qos.Sort;
import com.jenkin.common.exception.ExceptionEnum;
import com.jenkin.common.exception.LscException;
import com.jenkin.common.shiro.service.impl.BaseMenuServiceImpl;
import com.jenkin.systemservice.dao.system.MenuMapper;
import com.jenkin.systemservice.service.system.MenuService;
import com.jenkin.systemservice.service.system.PermissionService;
import com.jenkin.systemservice.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.system.MenuDto;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.MenuQo;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.SimpleQuery;
import org.springframework.transaction.annotation.Transactional;
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
    private UserService userService;
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
    @Transactional(rollbackFor = Exception.class)
    public MenuDto saveMenuInfo(MenuDto  menu) {
        setLevel(menu);
        setOrder(menu);
        saveOrUpdate( menu);
        return  menu;
    }

    /**
     * 设置当前菜单的排序
     * @param menu
     */
    private void setOrder(MenuDto menu) {
        List<MenuDto> subs = listByParentId(menu.getParent());
        int minOrder = 1;
        int maxOrder = menu.getId()==null?subs.size()+1:subs.size();
        int leftOrder = -1;
        int rightOrder = Integer.MAX_VALUE;
        int increment = 0;
        if(menu.getId()==null){
            leftOrder = menu.getMenuOrder();
            increment=1;
        }else{
            Integer menuOrder = getById(menu.getId()).getMenuOrder();
            leftOrder = Math.min(menuOrder, menu.getMenuOrder());
            rightOrder =Math.max(menuOrder, menu.getMenuOrder());
            increment = menuOrder>menu.getMenuOrder()?1:-1;
        }
        menu.setMenuOrder(menu.getMenuOrder()>maxOrder?maxOrder:(menu.getMenuOrder()<minOrder?minOrder:menu.getMenuOrder()));
        if (leftOrder==rightOrder){
            return;
        }
        for (MenuDto item : subs) {
            if(item.getMenuOrder()>=leftOrder&&item.getMenuOrder()<=rightOrder){
                item.setMenuOrder(item.getMenuOrder()+increment);
            }
        }
        saveOrUpdateBatch(BeanUtils.mapList(subs,MenuPo.class));
    }

    private void setLevel(MenuDto menu) {
        Integer parent = menu.getParent();
        int level = 0;
        if (parent>0) {
            MenuDto byId = getById(parent);
            if (byId!=null) {
                level = byId.getMenuLevel()+1;
            }
        }
        menu.setMenuLevel(level);
    }

    /**
     * 获取菜单的树形结构
     *
     * @return
     */
    @Override
    public List<MenuDto> listMenuTree() {
        List<MenuDto> menuDtos = listMenus();
        Map<Integer,List<Integer>> idsMap = new HashMap<>();
        menuDtos.stream().forEach(item ->  {
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
        for (MenuDto menuDto : menuDtos) {
            List<Integer> perIds = idsMap.get(menuDto.getId());
            if (perIds==null) {
                continue;
            }
            menuDto.setPermissionPos(permissionPos.stream().filter(item->perIds.contains(item.getId())).collect(Collectors.toList()));
        }
        List<MenuDto> parents =  menuDtos.stream()
                .filter(item->item.getParent().equals(MENU_ROOT_PARENT_ID)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(parents)) {
            parents.sort(Comparator.comparingInt(MenuPo::getMenuOrder));
            for (MenuDto parent : parents) {
                convertToTree(parent,menuDtos);
            }
        }
        return parents;
    }

    @Override
    public List<MenuDto> listMenus(){
        List<MenuPo> list = list();
        return  BeanUtils.mapList(list, MenuDto.class);
    }

    @Override
    public Map<Integer,MenuDto> getMenuMap(){
        Map<Integer,MenuDto> res = new HashMap<>();
        listMenus().forEach(item->{
            res.put(item.getId(),item);
        });
        return res;
    }

    private void convertToTree(MenuDto menuDto, List<MenuDto> menuDtos) {
        List<MenuDto> parents =  menuDtos.stream()
                .filter(item->item.getParent().equals(menuDto.getId())).collect(Collectors.toList());
        Set<MenuDto> parentSets =new TreeSet<MenuDto>(Comparator.comparingInt(MenuPo::getMenuOrder));
        parentSets.addAll(parents);

        if (!CollectionUtils.isEmpty(parentSets)) {
            menuDto.setSubList(new ArrayList<>(parentSets));
            for (MenuDto parent : parentSets) {
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
    @Transactional(rollbackFor = Exception.class)
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
        resetOrder(deleteIds,deleteMenus);
    }

    /**
     * 重新排序，需要找到那些父类下面发生了删除，若干发生了删除，需要对父类下面的子类重新排序
     * @param deleteIds 已删除的ID
     * @param deleteMenus 已删除的对象
     */
    private void resetOrder(Set<Integer> deleteIds, List<MenuDto> deleteMenus) {
        Set<Integer> parents =  getOccurDeleteParent(deleteIds,deleteMenus);
        for (Integer parent : parents) {
            List<MenuDto> menuDtos = listByParentId(parent);
            List<MenuPo> needUpdate = new ArrayList<>();
            for (int i = 1; i < menuDtos.size()+1; i++) {
                MenuDto menuDto = menuDtos.get(i - 1);
                if(!menuDto.getMenuOrder().equals(i)) {
                    menuDto.setMenuOrder(i);
                    needUpdate.add(BeanUtils.map(menuDto,MenuPo.class));
                }
            }
            saveOrUpdateBatch(needUpdate);
        }
    }

    private Set<Integer> getOccurDeleteParent(Set<Integer> deleteIds, List<MenuDto> deleteMenus) {
        Set<Integer> parents = new HashSet<>();
        for (MenuDto deleteMenu : deleteMenus) {
            if (!deleteIds.contains(deleteMenu.getParent())) {
                parents.add(deleteMenu.getParent());
            }
        }
        return parents;
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


    /**
     * 根据用户获取菜单
     *
     * @param userCode
     * @return
     */
    @Override
    public List<MenuDto> listMenuByUser(String userCode) {
        List<MenuDto> res = getUserMenus(userCode);
        if (CollectionUtils.isEmpty(res)) {
            throw new LscException(ExceptionEnum.FORBIDDEN_ACCESS_EXCEPTION);
        }
        res = resolveMenuParent(res);
        List<MenuDto> parents = res.stream().filter(item->item.getParent()<=0).collect(Collectors.toList());
        Set<MenuDto> parentSets =new TreeSet<MenuDto>(Comparator.comparingInt(MenuPo::getMenuOrder));
        parentSets.addAll(parents);

        for (MenuDto parent : parentSets) {
            convertToTree(parent,res);
        }
        return new ArrayList<>(parentSets);
    }

    /**
     * 获取当前父节点下面的子节点
     *
     * @param parent
     * @return
     */
    @Override
    public List<MenuDto> listByParentId(Integer parent) {
        MyQueryWrapper<MenuPo> queryWrapper = new MyQueryWrapper<>();
        queryWrapper.eq(parent!=null,MenuPo.Fields.parent,parent);
        queryWrapper.sort(Collections.singletonList(new Sort(MenuPo.Fields.menuOrder,"asc")));
        List<MenuPo> list = list(queryWrapper);
        list=list==null?new ArrayList<>():list;
        return BeanUtils.mapList(list,MenuDto.class);
    }

    /**
     * 解析用户的菜单，为子菜单补充父级菜单
     * @param menuList
     * @return
     */
    private List<MenuDto> resolveMenuParent(List<MenuDto> menuList) {
        Map<Integer, MenuDto> menuMap = getMenuMap();
        List<MenuDto> res = new ArrayList<>();
        Stack<MenuDto> menuStack = new Stack<>();
        menuList.forEach(item->{
            menuStack.push(item);
        });
        while(!menuStack.empty()){
            MenuDto pop = menuStack.pop();
            res.add(pop);
            if(pop.getParent()>0){
                menuStack.push(menuMap.get(pop.getParent()));
            }
        }
        return res;
    }

    private List<MenuDto> getUserMenus(String userCode) {
        List<MenuDto> res = new ArrayList<>();
        UserDto currentUserInfo = userService.getCurrentUserInfo(userCode);
        List<RoleDto> roles = currentUserInfo.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            for (RoleDto role : roles) {
                List<MenuPo> menus = role.getMenus();
                if (!CollectionUtils.isEmpty(menus)) {
                    for (MenuPo menu : menus) {
                        res.add(BeanUtils.map(menu,MenuDto.class));
                    }
                }
            }
        }
        return res;
    }
}
