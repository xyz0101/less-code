package com.jenkin.simpleshiro.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:40
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
public class UserDto implements Serializable {
    private User user;
    private List<RoleDto> roles;
}
