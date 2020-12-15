package com.jenkin.common.entity.pos.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.jenkin.common.entity.pos.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:30
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
@FieldNameConstants
@TableName("lsc_user")
@Table(name = "lsc_user",comment = "用户信息")
@ApiModel("用户信息")
public class UserPo extends BasePo implements Serializable {
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("用户编码")
    private String userCode;
    @ApiModelProperty("用户密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @ApiModelProperty("用户邮箱")
    private String userEmail;
    @ApiModelProperty("密码加盐")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;
    @ApiModelProperty("用户状态，1正常，0禁用")
    private Integer userStatus;
    @ApiModelProperty("用户头像")
    private String userHead;
    @ApiModelProperty("用户简介")
    private String userIntroduce;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer userPlaceId;

}
