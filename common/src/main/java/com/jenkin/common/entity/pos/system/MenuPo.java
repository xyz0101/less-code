package com.jenkin.common.entity.pos.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.jenkin.common.entity.pos.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:31
 * @description：
 * @modified By：
 * @version: 1.0
 */
@TableName("lsc_menu")
@FieldNameConstants
@Data
@ApiModel("菜单表")
public class MenuPo extends BasePo implements Serializable {

    @ApiModelProperty( "菜单名称")
    private String name;
    @ApiModelProperty( "菜单编号")
    private String code;
    @ApiModelProperty( "菜单父ID")
    private Integer parent;
    @ApiModelProperty( "菜单的层级")
    private Integer menuLevel;
    @ApiModelProperty( "菜单权限ID集合")
    private String permissions;
    @ApiModelProperty("菜单url")
    private String menuUrl;
    @ApiModelProperty( "菜单的图标")
    private String menuIcon;
    @ApiModelProperty( "菜单的顺序")
    private Integer menuOrder;
    @ApiModelProperty( "菜单类型，1：菜单，2、按钮")
    private Integer menuType;

    @TableField(select = false,exist = false)
    @ApiModelProperty("用户名称")
    private String userName;
    @TableField(select = false,exist = false)
    @ApiModelProperty("用户邮箱")
    private String userEmail;

    @ApiModelProperty("角色名称")
    @TableField(select = false,exist = false)
    private String roleName;



}
