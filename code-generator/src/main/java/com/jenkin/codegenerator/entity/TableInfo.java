package com.jenkin.codegenerator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jenkin
 * @className TableInfo
 * @description TODO
 * @date 2020/12/11 17:24
 */
@Data
@ApiModel("表信息")
public class TableInfo implements Serializable {
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
    @ApiModelProperty("列信息")
    private List<ColumnInfo> columns;
}
