package com.jenkin.common.entity.qos.system;
import java.lang.*;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：jenkin
 * @date ：Created at 2020-12-20 17:59:30
 * @description：
 * @modified By：
 * @version: 1.0
 */
@ApiModel("菜单表查询条件实体")
@Data
public class MenuQo {
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
}
