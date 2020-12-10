package com.jenkin.common.entity.pos.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.jenkin.common.entity.pos.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:31
 * @description：
 * @modified By：
 * @version: 1.0
 */
@TableName("lsc_permission")
@FieldNameConstants
@Table(name = "lsc_permission",comment = "权限信息表")
@Data
@ApiModel("权限信息表")
public class PermissionPo extends BasePo implements Serializable {

    @Column(comment = "权限名称")
    @ApiModelProperty("权限名称")
    private String name;
    @Column(comment = "权限编码")
    @ApiModelProperty("权限编码")
    private String code;

    public PermissionPo() {
    }

    public PermissionPo(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
