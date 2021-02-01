package com.jenkin.common.entity.pos.tasks;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jenkin.common.entity.pos.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import java.lang.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * @author ：jenkin
 * @date ：Created at 2021-02-01 15:31:32
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
@FieldNameConstants
@TableName("lsc_task")
@ApiModel("")
public class LscTaskPo extends BasePo implements Serializable {


    @ApiModelProperty(" ")
    private String taskCode;
    @ApiModelProperty(" 执行任务的用户")
    private String taskUser;
    @ApiModelProperty(" 任务执行时的参数")
    private String taskParamContent;
    @ApiModelProperty(" ")
    private Integer taskParentId;
    @ApiModelProperty(" 任务执行结果")
    private String taskResult;
    @ApiModelProperty(" 任务标题")
    private String taskTitle;
    @ApiModelProperty(" 任务类型：template（模板审核），generate（泛化）")
    private String taskType;
    @ApiModelProperty(" 任务的状态 进行中 ：2，等待中 ：1，未开始（待审核） ：0，已完成且成功 ：3，已完成但失败：4，已取消：-1")
    private Integer taskStatus;
    @ApiModelProperty(" 任务耗时")
    private Integer taskCost;
    @ApiModelProperty(" 任务开始时间")
    private LocalDateTime taskStartTime;
    @ApiModelProperty(" 任务结束时间")
    private LocalDateTime taskEndTime;
    
}
