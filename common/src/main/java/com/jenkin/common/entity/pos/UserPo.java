package com.jenkin.common.entity.pos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
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
    @Column(comment = "用户名称",name = "user_name",length = 50)
    @ApiModelProperty("用户名称")
    private String userName;
    @Column(comment = "用户编码",name = "user_code",length = 50)
    @ApiModelProperty("用户编码")
    private String userCode;
    @Column(comment = "用户密码",name = "password",length = 50)
    @ApiModelProperty("用户密码")
    private String password;
    @Column(comment = "用户邮箱",name = "user_email",length = 50)
    @ApiModelProperty("用户邮箱")
    private String userEmail;
    @Column(comment = "密码加盐",name = "salt",length = 50)
    @ApiModelProperty("密码加盐")
    private String salt;
    @Column(comment = "用户状态，1正常，0禁用",name = "user_status",length = 2,type = MySqlTypeConstant.TINYINT,defaultValue = "1")
    @ApiModelProperty("用户状态，1正常，0禁用")
    private Integer userStatus;


}
