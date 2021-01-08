package com.jenkin.common.entity.pos.file;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jenkin.common.entity.pos.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import java.lang.*;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * @author ：jenkin
 * @date ：Created at 2021-01-03 19:41:08
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
@FieldNameConstants
@TableName("lsc_files")
@ApiModel("文件表")
public class LscFilePo extends BasePo implements Serializable {


    @ApiModelProperty(" 文件类型，例如 image/jpeg")
    private String fileType;
    @ApiModelProperty(" 文件名称")
    private String fileName;
    @ApiModelProperty(" 文件编码")
    private String fileCode;
    @ApiModelProperty(" 文件大小，字节数")
    private Double fileSize;
    @ApiModelProperty(" 文件所归属的分类")
    private String fileCategories;
    @ApiModelProperty(" 是否是最新版")
    private Boolean newFlag;
    @ApiModelProperty("文件版本号")
    private Integer fileVersion;
    @ApiModelProperty("源文件编号")
    private String sourceFileCode;
}
