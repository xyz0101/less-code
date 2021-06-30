package com.jenkin.common.entity.vos.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
public class ExcelToWordVo {
    @ApiModelProperty("excel文件")
    private String excel;
    @ApiModelProperty("word文件")
    private String word;
    @ApiModelProperty("文件名使用哪个列生成")
    private String fileNameCol;
    @ApiModelProperty("内容行从哪一行开始")
    private Integer rowStartIndex;
}
