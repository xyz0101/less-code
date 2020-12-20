package com.jenkin.common.entity.qos.system;
import java.lang.*;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：jenkin
 * @date ：Created at 2020-12-20 18:26:24
 * @description：
 * @modified By：
 * @version: 1.0
 */
@ApiModel("权限信息表查询条件实体")
@Data
public class PermissionQo {
        @ApiModelProperty(" 权限名称")
    private String name;
    @ApiModelProperty(" 权限编码")
    private String code;
}
