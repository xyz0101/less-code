package com.jenkin.simpleshiro.shiro;

import com.jenkin.simpleshiro.ShiroUtils;
import com.jenkin.simpleshiro.dao.*;
import com.jenkin.simpleshiro.entity.*;
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
    private UsersRepository usersRepository;
    @Resource
    UserRoleRepository userRoleRepository;
    @Resource
    MenuRepository menuRepository;
    @Resource
    RoleRepository roleRepository;
    @Resource
    PermissionRepository permissionRepository;

    //角色权限和对应权限添加
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserDto userDto= (UserDto) principalCollection.getPrimaryPrincipal();
        Set<String> permissions = new HashSet<>();
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        UserDto userDto1 = refreshUserInfo(userDto.getUser().getName());
        userDto.setRoles(userDto1.getRoles());
        userDto.setUser(userDto1.getUser());


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
            return new SimpleAuthenticationInfo(user , user.getUser().getPassword() , ByteSource.Util.bytes(user.getUser().getSalt()),
                    getName());
        }
    }

    private UserDto getUser(String name, String password) {
        if (!StringUtils.hasLength(name)||!StringUtils.hasLength(password)) {
            return null;
        }

        return refreshUserInfo(name);
    }

    private UserDto refreshUserInfo(String name) {
        UserDto userDto = new UserDto();
        User user = usersRepository.getByName(name);
        if (user==null) {
            return null;
        }
        userDto.setUser(user);

        List<UserRole> allByUserId = userRoleRepository.findAllByUserId(user.getId().intValue());
        List<Integer> collect = allByUserId.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> allById = roleRepository.findAllById(collect);
        List<RoleDto> roleDtos = new ArrayList<>(allById.size());
        if (!CollectionUtils.isEmpty(allById)) {
            for (Role role : allById) {
                RoleDto roleDto = new RoleDto();
                if (StringUtils.hasLength(role.getMenus())) {
                    List<Integer> ids= new ArrayList<>();
                    for (String s : role.getMenus().split(",")) {
                        ids.add(Integer.parseInt(s));
                    }
                    List<Menu> allMenu = CollectionUtils.isEmpty(ids)?new ArrayList<>():menuRepository.findAllById(ids);
                    roleDto.setMenus(allMenu);
                    roleDto.setRole(role);
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
