package com.jenkin.common.entity.pos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.manager.system.SysMysqlCreateTableManagerImpl;
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
@Data
@TableName("lsc_user_role")
@FieldNameConstants
@Table(name = "lsc_user_role",comment = "用户角色中间表" )
public class UserRolePo extends BasePo implements Serializable {
    @Column
    private Integer userId;
    @Column
    private Integer roleId;


}
