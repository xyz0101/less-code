package com.jenkin.codegenerater.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jenkin
 * @className ColumInfo
 * @description TODO
 * @date 2020/12/11 17:25
 */
@Data
@ApiModel("列信息")
public class ColumInfo {
    /**
     * 是否主键
     */
    @ApiModelProperty("是否主键")
    private boolean isId;
    /**
     * 是否自增
     */
    @ApiModelProperty("是否自增")
    private boolean isAutoInc;
    @ApiModelProperty("列名称")
    private String name;
    @ApiModelProperty("列长度。只有类型是 字符或者是数字的时候才有")
    private Integer length;
    @ApiModelProperty("列类型")
    private String type;
    @ApiModelProperty("小数的长度")
    private Integer decimalLength;
    @ApiModelProperty("默认值")
    private String defaultValue;
    @ApiModelProperty("编码，默认 utf8mb4")
    private String encode="utf8mb4";
    @ApiModelProperty("编码排序规则，默认utf8mb4_bin")
    private String sort="utf8mb4_bin";
    @ApiModelProperty("注释")
    private String comment;
}
