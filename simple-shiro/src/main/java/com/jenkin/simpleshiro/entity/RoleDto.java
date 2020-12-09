package com.jenkin.simpleshiro.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:41
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
public class RoleDto implements Serializable {
    private Role role;
    private List<Menu> menus;
}
