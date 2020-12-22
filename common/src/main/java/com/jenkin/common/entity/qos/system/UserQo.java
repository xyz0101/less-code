package com.jenkin.common.entity.qos.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/10 21:20
 * @description：
 * @modified By：
 * @version: 1.0
 */
@ApiModel("用户查询条件实体")
@Data
public class UserQo {
    private String userName;
    @ApiModelProperty("用户编码")
    private String userCode;


    @ApiModelProperty("用户邮箱")
    private String userEmail;

    @ApiModelProperty("用户状态，1正常，0禁用")
    private Integer userStatus;
    @ApiModelProperty("用户头像")
    private String userHead;
    @ApiModelProperty("用户简介")
    private String userIntroduce;

}
