package com.jenkin.common.entity.dtos.file;

import com.jenkin.common.entity.pos.file.LscFilePo;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import java.lang.*;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2021-01-03 19:41:08
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
@ApiModel("文件表数据传输实体")
public class LscFileDto extends LscFilePo implements Serializable {

}
