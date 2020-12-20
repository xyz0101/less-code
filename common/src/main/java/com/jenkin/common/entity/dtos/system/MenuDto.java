package com.jenkin.common.entity.dtos.system;

import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.entity.pos.system.PermissionPo;

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
public class MenuDto extends MenuPo implements Serializable {
    /**
     * 桑倩菜单需要的权限
     */
    private List<PermissionPo> permissionPos;

    private List<MenuDto> subList;

}
