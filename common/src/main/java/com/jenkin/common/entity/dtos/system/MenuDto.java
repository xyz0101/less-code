package com.jenkin.common.entity.dtos.system;

import com.jenkin.common.entity.pos.system.MenuPo;
import com.jenkin.common.entity.pos.system.PermissionPo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:42
 * @description：
 * @modified By：
 * @version: 1.0
 */

@Data
@ApiModel("菜单")
public class MenuDto extends MenuPo implements Serializable {
    @ApiModelProperty("是否是一级菜单，如果是一级菜单那么不能选择父类，默认是根目录")
    private Boolean level1Flag;

    /**
     * 桑倩菜单需要的权限
     */
    private List<PermissionPo> permissionPos;

    private List<MenuDto> subList;

    /**
     * 重写equals，在ID不为空的情况下，如果ID相等，那么对象就相相等
     */
    @Override
    public boolean equals(Object o) {

        if(this.getId()!=null&&o!=null&&( (MenuDto) o).getId().equals(this.getId())){
            return true;
        }
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MenuDto menuDto = (MenuDto) o;
        return Objects.equals(permissionPos, menuDto.permissionPos) &&
                Objects.equals(subList, menuDto.subList);
    }

    /**
     * 重写hashCode，在ID不为空的情况下返回ID作为hashCode
     * @return
     */
    @Override
    public int hashCode() {
        if(this.getId()!=null ){
            return this.getId();
        }
        return Objects.hash(super.hashCode(), permissionPos, subList);
    }
}
