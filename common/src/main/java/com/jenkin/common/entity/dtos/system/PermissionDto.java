package com.jenkin.common.entity.dtos.system;

import com.jenkin.common.entity.pos.system.PermissionPo;
import com.jenkin.common.entity.pos.system.UserPo;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import java.lang.*;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2020-12-20 18:26:24
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
@ApiModel("权限信息表数据传输实体")
public class PermissionDto extends PermissionPo implements Serializable {

}
