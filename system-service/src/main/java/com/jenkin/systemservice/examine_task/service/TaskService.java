package com.jenkin.systemservice.examine_task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.jenkin.common.entity.dtos.tasks.LscTaskDto;
import com.jenkin.common.entity.pos.tasks.LscTaskPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.tasks.LscTaskQo;
import com.jenkin.systemservice.examine_task.service.impl.TaskServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jenkin
 * @className LabelService
 * @description TODO
 * @date 2020/6/28 15:15
 */
public interface TaskService extends IService<LscTaskPo> {
    /**
     * 获取单个的任务的结果
     * @param taskCode
     * @return
     */
    TaskServiceImpl.TaskExecuteResult getSingleTaskResultByCode(String taskCode);

    /**
     * 开启任务
     * @param codes
     */
    void startTask(String[] codes,FunctionExecute execute);

    /**
     * 获取单个任务
     * @param code
     * @return
     */
    LscTaskDto getSingleTaskByCode(String code);

    /**
     * 创建一个任务
     * @param t
     */
    void  createTask(LscTaskInfo t);
    /**
     * 创建一个任务
     * @param t
     */
    void  createAndStartTask(LscTaskInfo t,FunctionExecute execute);
    /**
     * 取消任务
     * @param codes
     */
    void cancelTask(String[] codes);

    /**
     * 获取任务列表
     * @param baseQO
     * @return
     */
    Page<LscTaskDto> listTasks(BaseQo<LscTaskQo> baseQO);

    /**
     * 重新启动选中的任务
     * @param codes
     */
    void reStartTask(String[] codes,FunctionExecute execute);

    /**
     * 取消该用户的所有任务
     */
    void cancelAllTask();

    /**
     * 删除宣州的任务
     * @param codes
     */
    void deleteTask(String[] codes);

    @Data
    static class LscTaskInfo<T>{
        T taskParam;
        @ApiModelProperty(" 任务标题")
        private String taskTitle;
        @ApiModelProperty(" 任务类型：template（模板审核），generate（泛化）")
        private String taskType;
    }
    @Data
    static class TaskExecuteResult{
        private Object result;
    }
}
