package com.jenkin.common.entity.pos.generate;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jenkin.common.entity.pos.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * @author jenkin
 * @className ColumnInfoDto
 * @description TODO
 * @date 2020/12/11 17:25
 */
@Data
@ApiModel("列信息")
@TableName("lsc_column_info")
@FieldNameConstants
public class ColumnInfoPo extends BasePo implements Serializable {
    /**
     * 表ID
     */
    @ApiModelProperty("关联的表的ID")
    private Integer tableId;

    /**
     * 是否主键
     */
    @ApiModelProperty("是否主键")
    private Boolean idFlag;
    /**
     * 是否自增
     */
    @ApiModelProperty("是否自增")
    private Boolean autoIncFlag;
    @ApiModelProperty("是否空值")
    private Boolean nullFlag;

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
    private String comments;
    @ApiModelProperty("字段的java类型")
    private String javaType;
    @ApiModelProperty("字段的java列名称")
    private String javaColName;

}
