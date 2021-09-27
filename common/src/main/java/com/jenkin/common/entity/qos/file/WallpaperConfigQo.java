package com.jenkin.common.entity.qos.file;
import java.lang.*;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：jenkin
 * @date ：Created at 2021-09-26 19:21:47
 * @description：
 * @modified By：
 * @version: 1.0
 */
@ApiModel("查询条件实体")
@Data
public class WallpaperConfigQo {
        @ApiModelProperty(" ")
    private Boolean onFlag;
    @ApiModelProperty(" ")
    private String strategyValue;
}
