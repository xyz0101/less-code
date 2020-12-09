/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.jenkin.simpleshiro;

import cn.hutool.core.bean.BeanUtil;
import com.jenkin.simpleshiro.entity.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Shiro工具类
 *
 * @author Mark sunlightcs@gmail.com
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

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static UserDto getUserEntity() {
		return (UserDto)SecurityUtils.getSubject().getPrincipal();
	}
	public static UserDto getUserEntityNoPermissionStr() {
		UserDto principal = (UserDto) SecurityUtils.getSubject().getPrincipal();
		UserDto userDto = new UserDto();
		List<RoleDto> roles = principal.getRoles();
		List<RoleDto> newRoles = new ArrayList<>();
		if (!CollectionUtils.isEmpty(roles)) {
			for (RoleDto role : roles) {
				RoleDto newRole = new RoleDto();
				List<Menu> menus = role.getMenus();
				List<Menu> menusNew = new ArrayList<>();
				if (!CollectionUtils.isEmpty(menus)) {
					for (Menu menu : menus) {
						Menu newMenu = BeanUtil.copyProperties(menu, Menu.class, "permissions");
						menusNew.add(newMenu);
					}
				}
				newRole.setMenus(menusNew);
				newRole.setRole(BeanUtil.copyProperties(role.getRole(), Role.class));
				newRoles.add(newRole);
			}
		}
		userDto.setRoles(newRoles);
		User user = BeanUtil.copyProperties(principal.getUser(), User.class, "password","salt","id");
		userDto.setUser(user);
		return userDto;
	}
	public static String getUserId() {
		return getUserEntity().getUser().getName();
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
