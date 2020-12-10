package com.jenkin.common.entity.dtos.system;

import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.entity.pos.system.RolePo;
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
public class RoleDto extends RolePo implements Serializable {
    private List<MenuPo> menus;
}
