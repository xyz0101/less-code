package com.jenkin.systemservice.examine_task.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.dtos.tasks.LscTaskDto;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.tasks.LscTaskQo;
import com.jenkin.systemservice.examine_task.service.TaskService;
import com.jenkin.systemservice.examine_task.service.impl.TaskServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jenkin
 * @className TaskController
 * @description TODO
 * @date 2020/9/22 14:43
 */
@RestController
@RequestMapping("/task")
@Api(tags = "审核任务相关接口")
@CrossOrigin
public class TaskController {


    @Autowired
    private TaskService taskService;

    /**
     * 新增一个任务
     * @param taskInfo
     * @return
     */
    @PostMapping("/addTask")
    @ApiOperation("添加一个审核任务")
    public Response addTask(@RequestBody TaskService.LscTaskInfo taskInfo){
        taskService.createTask(taskInfo);
        return Response.ok();
    }

    /**
     * 启动选中的任务
     * @param codes
     * @return
     */
    @PostMapping("/startTask")
    @ApiOperation("启动选中的任务")
    public Response startTask(@RequestBody String[] codes){
        taskService.startTask(codes,null);
        return Response.ok();
    }
    /**
     * 启动选中的任务
     * @param codes
     * @return
     */
    @PostMapping("/restartTask")
    @ApiOperation("重新启动选中的任务")
    public Response reStartTask(@RequestBody String[] codes){
        taskService.reStartTask(codes,null);
        return Response.ok();
    }

    /**
     * 取消选中的任务
     * @param codes
     * @return
     */
    @PostMapping("/cancelTask")
    @ApiOperation("取消选中的任务")
    public Response cancelTask(@RequestBody String[] codes){
        taskService.cancelTask(codes);
        return Response.ok();
    }
    /**
     * 取消全部
     * @return
     */
    @GetMapping("/cancelAllTask")
    @ApiOperation("取消所有的任务")
    public Response cancelAllTask(){
        taskService.cancelAllTask();
        return Response.ok();
    }
    /**
     * 取消全部
     * @return
     */
    @PostMapping("/deleteTask")
    @ApiOperation("删除选中的任务")
    public Response deleteTask(@RequestBody String[] codes){
        taskService.deleteTask(codes);
        return Response.ok();
    }

    @PostMapping("/listTasks")
    @ApiOperation("获取所有的任务列表（根据状态获取）")
    public Response<Page<LscTaskDto>> listTasks(@RequestBody BaseQo<LscTaskQo> baseQO){
        Page<LscTaskDto> pageData = taskService.listTasks(baseQO);
        return Response.ok(pageData);
    }

    /**
     * 获取单个任务的结果
     * @param taskCode
     * @return
     */
    @GetMapping("/getSingleTaskResult")
    @ApiOperation("获取单个任务的结果")
    public Response<TaskServiceImpl.TaskExecuteResult> getSingleTaskResult(String taskCode){
        TaskServiceImpl.TaskExecuteResult taskExecuteResult =taskService.getSingleTaskResultByCode(taskCode);
        return Response.ok(taskExecuteResult);
    }


    /**
     * 获取单个任务的结果
     * @param taskCode
     * @return
     */
    @GetMapping("/getSingleTask")
    @ApiOperation("获取单个任务的结果")
    public Response<LscTaskDto> getSingleTask(String taskCode){
        LscTaskDto task = taskService.getSingleTaskByCode(taskCode);
        return Response.ok(task);
    }







}
