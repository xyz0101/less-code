package com.jenkin.common.entity.vos.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：jenkin
 * @date ：Created at 2021/6/28 17:37
 * @menu
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
@ApiModel("excel生成word的vo")
public class RenameFileByExcelVo {
    @ApiModelProperty("excel")
    private String excel;
    @ApiModelProperty("压缩的文件")
    private String zipFile;
    @ApiModelProperty("文件名使用哪个列生成")
    private String fileNameCol;
    @ApiModelProperty("内容行从哪一行开始")
    private Integer rowStartIndex;
}
