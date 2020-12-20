package com.jenkin.common.entity.qos.system;
import java.lang.*;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：jenkin
 * @date ：Created at 2020-12-20 18:21:07
 * @description：
 * @modified By：
 * @version: 1.0
 */
@ApiModel("角色信息查询条件实体")
@Data
public class RoleQo {
        @ApiModelProperty(" 角色名称")
    private String roleName;
    @ApiModelProperty(" 角色编码")
    private String roleCode;
    @ApiModelProperty(" 角色菜单")
    private String menuStr;
}
