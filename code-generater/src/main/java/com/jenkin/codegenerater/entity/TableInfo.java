package com.jenkin.codegenerater.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jenkin
 * @className TableInfo
 * @description TODO
 * @date 2020/12/11 17:24
 */
@Data
@ApiModel("表信息")
public class TableInfo {
    private String tableName;
    @ApiModelProperty("存储引擎，默认Innodb")
    private String engin;
    @ApiModelProperty("编码，默认 utf8mb4")
    private String encode="utf8mb4";
    @ApiModelProperty("编码排序规则，默认utf8mb4_bin")
    private String sort="utf8mb4_bin";
    @ApiModelProperty("注释")
    private String comment;
    private List<ColumInfo> colums;
}
