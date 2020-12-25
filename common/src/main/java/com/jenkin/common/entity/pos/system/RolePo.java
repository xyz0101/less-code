package com.jenkin.common.entity.pos.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.annotation.TableComment;
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
@TableName("lsc_role")
@FieldNameConstants
@Data
@Table(name = "lsc_role",comment = "角色信息")
@TableComment("角色信息")
@ApiModel("角色信息")
public class RolePo extends BasePo implements Serializable {

    @ApiModelProperty("角色名称")
    @Column(comment = "角色名称",name = "role_name",length = 50)
    private String roleName;
    @ApiModelProperty("角色编码")
    @Column(comment = "角色编码",name = "role_code",length = 50)
    private String roleCode;
    @ApiModelProperty("角色菜单")
    @Column(comment = "角色菜单",name = "menu_str",length = 500)
    private String menuStr;
    @ApiModelProperty("角色菜单名称")
    private String menuNames;
}
