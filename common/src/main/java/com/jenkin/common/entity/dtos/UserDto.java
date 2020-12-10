package com.jenkin.common.entity.dtos;

import com.jenkin.common.entity.pos.UserPo;
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
public class UserDto extends UserPo implements Serializable {
    private List<RoleDto> roles;
}
