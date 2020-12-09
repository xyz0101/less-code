package com.jenkin.menuservice.shiro;

import com.jenkin.menuservice.BeanUtils;
import com.jenkin.menuservice.ShiroUtils;
import com.jenkin.menuservice.entity.dtos.RoleDto;
import com.jenkin.menuservice.entity.dtos.UserDto;
import com.jenkin.menuservice.entity.pos.MenuPo;
import com.jenkin.menuservice.entity.pos.RolePo;
import com.jenkin.menuservice.entity.pos.UserRolePo;
import com.jenkin.menuservice.service.*;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

//实现AuthorizingRealm接口用户用户认证
public class MyShiroRealm extends AuthorizingRealm {

    //用于用户查询

    @Resource
    private UserService userService;
    @Resource
    UserRoleService userRoleService;
    @Resource
    MenuService menuService;
    @Resource
    RoleService roleService;
    @Resource
    PermissionService permissionService;

    //角色权限和对应权限添加
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserDto userDto= (UserDto) principalCollection.getPrimaryPrincipal();
        Set<String> permissions = new HashSet<>();
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        UserDto userDto1 = refreshUserInfo(userDto.getUserCode());
        if (userDto1==null) {
            return null;
        }
        BeanUtils.map(userDto1,userDto);
        userDto.setRoles(userDto1.getRoles());
        if (!CollectionUtils.isEmpty(userDto.getRoles())) {
            for (RoleDto role : userDto.getRoles()) {
                if (!CollectionUtils.isEmpty(role.getMenus())) {
                    role.getMenus().forEach(item->{
                        if (StringUtils.hasLength(item.getPermissions())) {
                            String[] split = item.getPermissions().split(",");
                            permissions.addAll(Arrays.asList(split));
                        }
                    });
                }
            }
        }
        simpleAuthorizationInfo.addStringPermissions(permissions);
        System.out.println("权限："+simpleAuthorizationInfo.getStringPermissions());
        return simpleAuthorizationInfo;
    }

    //用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String name = ((UsernamePasswordToken)authenticationToken).getUsername();
        String password = new String(((UsernamePasswordToken)authenticationToken).getPassword());
        UserDto user = getUser(name,password);
        if (user == null) {
            throw new RuntimeException("用户不存在或密码错误");
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            return new SimpleAuthenticationInfo(user , user.getPassword() ,
                    ByteSource.Util.bytes(user.getSalt()),
                    getName());
        }
    }

    private UserDto getUser(String name, String password) {
        if (!StringUtils.hasLength(name)||!StringUtils.hasLength(password)) {
            return null;
        }

        return refreshUserInfo(name);
    }

    private UserDto refreshUserInfo(String code) {
        UserDto userDto = userService.getByCode(code);
        if (userDto==null) {
            return null;
        }
        List<UserRolePo> allByUserId = userRoleService.listByUserId(userDto.getId() );
        List<Integer> collect = allByUserId.stream().map(UserRolePo::getRoleId).collect(Collectors.toList());
        List<RolePo> allById = roleService.listByIds(collect);
        List<RoleDto> roleDtos = new ArrayList<>(allById.size());
        if (!CollectionUtils.isEmpty(allById)) {
            for (RolePo role : allById) {
                RoleDto roleDto = BeanUtils.map(role,RoleDto.class);
                if (StringUtils.hasLength(role.getMenus())) {
                    List<Integer> ids= new ArrayList<>();
                    for (String s : role.getMenus().split(",")) {
                        ids.add(Integer.parseInt(s));
                    }
                    List<MenuPo> allMenu = CollectionUtils.isEmpty(ids)?new ArrayList<>()
                            :menuService.listByIds(ids);
                    roleDto.setMenus(allMenu);
                    roleDtos.add(roleDto);
                }
            }
        }
        userDto.setRoles(roleDtos);
        return userDto;
    }

    /**
     * 设置密码的加密解密方式
     * @param credentialsMatcher
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);
        shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }
}
