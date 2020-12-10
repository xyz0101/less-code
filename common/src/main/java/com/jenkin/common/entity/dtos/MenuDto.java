package com.jenkin.common.entity.dtos;

import com.jenkin.common.entity.pos.MenuPo;
import com.jenkin.common.entity.pos.PermissionPo;

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

    private List<PermissionPo> permissionPos;
}
