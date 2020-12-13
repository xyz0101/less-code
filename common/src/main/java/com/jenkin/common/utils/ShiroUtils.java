/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.jenkin.common.utils;


import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.jenkin.common.entity.dtos.system.RoleDto;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.shiro.token.MyToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Shiro工具类
 *
 * @author jenkin
 */
public class ShiroUtils {



	/**  加密算法 */
	public final static String hashAlgorithmName = "SHA-256";
	/**  循环次数 */
	public final static int hashIterations = 16;




	public static String sha256(String password, String salt) {


		return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toString();
	}

	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public static void login(MyToken myToken) {
		SecurityUtils.getSubject().login(myToken);

	}



	public static UserDto getUserEntity() {
		return (UserDto) SecurityUtils.getSubject().getPrincipal();
	}
	public static UserDto getUserEntityNoPermissionStr() {
		UserDto principal =  getUserEntity();
		return removeSentiveMessage(principal);
	}

	public static UserDto removeSentiveMessage(UserDto principal) {
		UserDto userDto = BeanUtils.map(principal,UserDto.class,"password","salt","id");
		List<RoleDto> roles = principal.getRoles();
		List<RoleDto> newRoles = new ArrayList<>();
		if (!CollectionUtils.isEmpty(roles)) {
			for (RoleDto role : roles) {
				RoleDto newRole =  BeanUtils.map(role, RoleDto.class);
				List<MenuPo> menus = role.getMenus();
				List<MenuPo> menusNew = new ArrayList<>();
				if (!CollectionUtils.isEmpty(menus)) {
					for (MenuPo menu : menus) {
						MenuPo newMenu = BeanUtils.map(menu, MenuPo.class, "permissions");
						menusNew.add(newMenu);
					}
				}
				newRole.setMenus(menusNew);
				newRoles.add(newRole);
			}
		}
		userDto.setRoles(newRoles);
		return userDto;
	}

	public static String getUserCode() {
		try {
			UserDto userEntity = getUserEntity();
			if (userEntity==null) {
				return "system";
			}
			return userEntity.getUserCode();
		}catch (Exception e){
			return "system";
		}


	}
	
	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}

	public static boolean isLogin() {
		return SecurityUtils.getSubject().getPrincipal() != null;
	}

	public static void logout() {
		SecurityUtils.getSubject().logout();
	}
	


}
