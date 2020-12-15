package com.jenkin.common.entity.pos;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author jenkin
 * @className BasePO
 * @description 
 * @date 2020/6/23 15:29
 */
@MappedSuperclass
@FieldNameConstants
@Data
public class BasePo {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    @Column(comment = "主键")
    @IsKey
    @IsAutoIncrement
    private Integer id;
    @ApiModelProperty("删除标识")
    @TableField(fill= FieldFill.INSERT)
    @Column(comment = "删除标识")
    @TableLogic
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer deleteFlag;
    @ApiModelProperty(value = "创建人")
    @TableField(fill= FieldFill.INSERT)
    @Column(comment = "创建人",length = 50)
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill= FieldFill.INSERT)
    @Column(comment = "创建时间",type = MySqlTypeConstant.DATE )
    private Date creationDate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill= FieldFill.INSERT_UPDATE)
    @Column(comment = "更新时间",type = MySqlTypeConstant.DATE )
    private Date lastUpdateDate;

    @ApiModelProperty(value = "更新人")
    @TableField(fill= FieldFill.INSERT_UPDATE)
    @Column(comment = "更新人",length = 50)
    private String lastUpdatedBy;

    @ApiModelProperty(value = "版本号")
    @Version
    @Column(comment = "版本号",type = MySqlTypeConstant.INT )
    private Integer versionNumber;

}
