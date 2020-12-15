package com.jenkin.codegenerator.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/13 15:51
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data

@ApiModel("字段类型信息")
public class MysqlType {
    public MysqlType(String sqlType, String javaType, Integer defaultLength, Integer needDecimalLength, Boolean needIncrement, Boolean needEnCode) {
        this.sqlType = sqlType;
        this.javaType = javaType;
        this.defaultLength = defaultLength;
        this.needDecimalLength = needDecimalLength;
        this.needIncrement = needIncrement;
        this.needEnCode = needEnCode;
    }

    public MysqlType(String sqlType, String javaType, Integer defaultLength, Integer needDecimalLength, Boolean needIncrement) {
        this.sqlType = sqlType;
        this.javaType = javaType;
        this.defaultLength = defaultLength;
        this.needDecimalLength = needDecimalLength;
        this.needIncrement = needIncrement;
        this.needEnCode=false;
    }

    @ApiModelProperty("SQL类型")
    private String sqlType;
    @ApiModelProperty("java字段类型")
    private String javaType;
    @ApiModelProperty("是否可以选择字段长度,如果为空，就是不可以，否则就是默认值")
    private Integer defaultLength;
    @ApiModelProperty("是否可以选小数位长度,如果为空，就是不可以，否则就是默认值")
    private Integer needDecimalLength;
    @ApiModelProperty("是否有自增选项")
    private Boolean needIncrement=false;
    @ApiModelProperty("是否需要编码")
    private Boolean needEnCode;


}
