package com.jenkin.common.shiro;


import com.jenkin.common.entity.dtos.system.RoleDto;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.entity.pos.system.PermissionPo;
import com.jenkin.common.entity.pos.system.RolePo;
import com.jenkin.common.entity.pos.system.UserRolePo;
import com.jenkin.common.shiro.service.*;
import com.jenkin.common.shiro.token.MyToken;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.Redis;
import com.jenkin.common.utils.ShiroUtils;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.jenkin.common.shiro.token.MyTokenFilter.TOKEN_KEY;

/**
 * 自定义shiro认证
 */
public class MyShiroRealm extends AuthorizingRealm {



    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    BasePermissionService basePermissionService;
    @Autowired
    private Redis redis;
    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof MyToken;
    }

    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserDto userDto= (UserDto) principalCollection.getPrimaryPrincipal();
        Set<String> permissions = new HashSet<>();

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
                            List<Integer> collect = Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList());
                            List<PermissionPo> permissionPos = basePermissionService.listByIds(collect);
                            List<String> perms = permissionPos.stream().map(PermissionPo::getCode).collect(Collectors.toList());
                            permissions.addAll(perms);
                        }
                    });
                }
            }
        }
        simpleAuthorizationInfo.addStringPermissions(permissions);
        System.out.println("权限："+simpleAuthorizationInfo.getStringPermissions());
        return simpleAuthorizationInfo;
    }

    /**
     * 用户登录
     * 角色权限和对应权限添加
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String code = ((MyToken)authenticationToken).getPrincipal().getUserCode();
        String password = ((MyToken)authenticationToken).getCredentials() ;
        UserDto user = getUser(code,password);
        if (user == null) {
            throw new RuntimeException("用户不存在或密码错误");
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
                    ByteSource.Util.bytes(user.getSalt()),
                    getName());
            redis.set(TOKEN_KEY+code, authenticationToken);
            return simpleAuthenticationInfo;
        }
    }

    private UserDto getUser(String name, String password) {
        if (!StringUtils.hasLength(name)||!StringUtils.hasLength(password)) {
            return null;
        }

        return refreshUserInfo(name);
    }

    private UserDto refreshUserInfo(String code) {
       return baseUserService.getCurrentUserInfo(code);
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
