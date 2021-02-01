package com.jenkin.systemservice.history.controller;

import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.dtos.tasks.LscTaskDto;
import com.jenkin.common.exception.ExceptionEnum;
import com.jenkin.common.exception.LscException;
import com.jenkin.systemservice.examine_task.service.TaskService;
import com.jenkin.systemservice.history.service.HistoryService;
import com.jenkin.systemservice.history.service.OAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.jenkin.systemservice.history.service.HistoryService.ACTIVITY_ID;

/**
 * @author jenkin
 * @className HistoryController
 * @description TODO
 * @date 2021/2/1 17:12
 */
@RestController
@RequestMapping("/history")
@CrossOrigin
@Api(tags = "四史答题相关接口")
public class HistoryController {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private TaskService taskService;



    @GetMapping("/getToken")
    @ApiOperation("获取token")
    public Response<String> getToken(String uid){
        if (StringUtils.isEmpty(uid)){
            throw new LscException(ExceptionEnum.ERROR_PARAM_EXCEPTION);
        }
        try {
            return Response.ok(historyService.getToken(uid,
                    "https:%2F%2Fnode2d-public.hep.com.cn%2Favatar-5fb278bff18a2c12929f495f-1605531839324",ACTIVITY_ID).getToken());
        }catch (Exception e){
            e.printStackTrace();
            throw new LscException(ExceptionEnum.QRCODE_LOGIN_ERROR_EXCEPTION);
        }


    }

    @GetMapping("/getUser")
    @ApiOperation("获取用户信息")
    public Response<HistoryService.UserInfo> getUserInfo(){
        Response<HistoryService.UserInfo> user = historyService.getUser();
        if (user.getCode().equals("0")) {
            return Response.ok(user.getData());
        }
        throw new LscException(ExceptionEnum.QRCODE_LOGIN_ERROR_EXCEPTION);
    }
    @GetMapping("/getGradeInfo")
    @ApiOperation("获取积分信息")
    public Response<HistoryService.Person> getGradeInfo(String activityId){
        Response<HistoryService.Person> grade = historyService.grade(activityId);
        if (grade.getCode().equals("0")) {
            return Response.ok(grade.getData());
        }
        throw new LscException(ExceptionEnum.QRCODE_LOGIN_ERROR_EXCEPTION);
    }
    @GetMapping("/getQrCode")
    @ApiOperation("获取二维码")
    public Response<OAuthService.QrCode> getQrCode(){
        return oAuthService.getQrCode(UUID.randomUUID().toString(),true,false);
    }


    public Response startStudy(@RequestBody TaskParam taskParam){
        TaskService.LscTaskInfo<TaskParam> taskInfo = new TaskService.LscTaskInfo<>();
        taskInfo.setTaskTitle("四史刷题");
        taskInfo.setTaskType("1");
        taskInfo.setTaskParam(taskParam);
        taskService.createAndStartTask(taskInfo,(task,user)->{
            return runTask(task,user);
        });
        return Response.ok();
    }

    private TaskService.TaskExecuteResult runTask(LscTaskDto task, String user) {

        return null;
    }


    @Data
    @ApiModel("刷题任务参数")
    public static class TaskParam{
        @ApiModelProperty("最大积分")
        private Integer maxGrade;
        @ApiModelProperty("每道题目最小时间间隔，默认1s，不得低于1s")
        private Integer miniTimeSpace;
        @ApiModelProperty("当前已有的积分")
        private Integer currentGrade;

    }






}
