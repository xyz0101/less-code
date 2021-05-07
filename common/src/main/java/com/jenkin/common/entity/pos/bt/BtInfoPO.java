package com.jenkin.common.entity.pos.bt;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/26 20:32
 * @description：
 * @modified By：
 * @version: 1.0
 */
@TableName("dht")
@Data
@FieldNameConstants
public class BtInfoPO {


    @ApiModelProperty(value = "创建时间")
    @TableField( value = "creation_data")
    private LocalDateTime creationDate;

    private Long id;

    private String btText;

    private String btData;

}
