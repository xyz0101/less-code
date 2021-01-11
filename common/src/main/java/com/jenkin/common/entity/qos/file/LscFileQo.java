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
    @ApiModelProperty(" MD5校验码")
    private String md5Code;
    @ApiModelProperty(" 文件版本号")
    private Integer fileVersion;
    @ApiModelProperty(" 出示文件编码")
    private String sourceFileCode;
}
