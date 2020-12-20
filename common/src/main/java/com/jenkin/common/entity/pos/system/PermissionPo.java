package com.jenkin.common.entity.pos.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.jenkin.common.entity.pos.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:31
 * @description：
 * @modified By：
 * @version: 1.0
 */
@TableName("lsc_permission")
@FieldNameConstants
@Table(name = "lsc_permission",comment = "权限信息表")
@Data
@ApiModel("权限信息表")
public class PermissionPo extends BasePo implements Serializable {

    @ApiModelProperty(" 菜单地址")
    private String menuUrl;
    @ApiModelProperty(" 菜单名称")
    private String name;
    @ApiModelProperty(" 菜单编号")
    private String code;
    @ApiModelProperty(" 菜单父ID")
    private Integer parent;
    @ApiModelProperty(" 菜单权限ID集合")
    private String permissions;
    @ApiModelProperty(" 菜单的图标")
    private String menuIcon;
    @ApiModelProperty(" 菜单的顺序")
    private Integer menuOrder;
    @ApiModelProperty(" 菜单类型，1：菜单，2、按钮")
    private Integer menuType;
    public PermissionPo() {
    }

    public PermissionPo(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
