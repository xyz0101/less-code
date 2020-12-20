package com.jenkin.common.entity.pos.generate;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jenkin.common.entity.pos.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.List;

/**
 * @author jenkin
 * @className TableInfoDto
 * @description TODO
 * @date 2020/12/11 17:24
 */
@Data
@ApiModel("表信息")
@TableName("lsc_table_info")
@FieldNameConstants
public class TableInfoPo extends BasePo implements Serializable {
    @ApiModelProperty("数据库")
    private String tableSchema;
    @ApiModelProperty("表名称")
    private String tableName;
    @ApiModelProperty("存储引擎，默认Innodb")
    private String engine;
    @ApiModelProperty("编码，默认 utf8mb4")
    private String encode="utf8mb4";
    @ApiModelProperty("编码排序规则，默认utf8mb4_bin")
    private String tableCollation="utf8mb4_bin";
    @ApiModelProperty("注释")
    @TableField(value = "table_comment")
    private String tableComments;
    @ApiModelProperty("是否未创建")
    private Boolean unCreateFlag;

}
