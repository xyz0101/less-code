package com.jenkin.menuservice.entity.dtos;

import com.jenkin.menuservice.entity.pos.MenuPo;
import com.jenkin.menuservice.entity.pos.RolePo;
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
