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
 * @date ：Created at 2021-09-26 19:21:47
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
@FieldNameConstants
@TableName("lsc_wallpaper_config")
@ApiModel("")
public class WallpaperConfigPo extends BasePo implements Serializable {


    @ApiModelProperty(" ")
    private Boolean onFlag;
    @ApiModelProperty(" ")
    private String strategyValue;
    
}
