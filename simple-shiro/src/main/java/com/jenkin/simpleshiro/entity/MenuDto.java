package com.jenkin.simpleshiro.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:42
 * @description：
 * @modified By：
 * @version: 1.0
 */

@Data
public class MenuDto implements Serializable {
    private Menu menu;
    private List<Permission> permissions;
}
