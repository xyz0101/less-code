package com.jenkin.common.entity.qos.file;
import java.lang.*;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：jenkin
 * @date ：Created at 2021-01-03 19:52:23
 * @description：
 * @modified By：
 * @version: 1.0
 */
@ApiModel("查询条件实体")
@Data
public class LscFileQo {
        @ApiModelProperty(" ")
    private Integer tableId;
    @ApiModelProperty(" ")
    private Boolean idFlag;
    @ApiModelProperty(" ")
    private Boolean autoIncFlag;
    @ApiModelProperty(" ")
    private Boolean nullFlag;
    @ApiModelProperty(" ")
    private String name;
    @ApiModelProperty(" ")
    private Integer length;
    @ApiModelProperty(" ")
    private String type;
    @ApiModelProperty(" ")
    private Integer decimalLength;
    @ApiModelProperty(" ")
    private String defaultValue;
    @ApiModelProperty(" ")
    private String encode;
    @ApiModelProperty(" ")
    private String sort;
    @ApiModelProperty(" ")
    private String comments;
    @ApiModelProperty(" ")
    private String javaType;
    @ApiModelProperty(" ")
    private String javaColName;
}
