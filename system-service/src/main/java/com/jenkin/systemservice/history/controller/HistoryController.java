package com.jenkin.systemservice.history.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jenkin.common.anno.EnableErrorCatch;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.dtos.tasks.LscTaskDto;
import com.jenkin.common.entity.vos.aibizhi.Res;
import com.jenkin.common.entity.vos.tasks.TriggerVO;
import com.jenkin.common.enums.TaskStatusEnum;
import com.jenkin.common.exception.ExceptionEnum;
import com.jenkin.common.exception.LscException;
import com.jenkin.common.shiro.utils.ShiroUtils;
import com.jenkin.common.utils.Redis;
import com.jenkin.systemservice.examine_task.service.TaskService;
import com.jenkin.systemservice.examine_task.socket.MessageSender;
import com.jenkin.systemservice.history.service.HistoryService;
import com.jenkin.systemservice.history.service.OAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.jenkin.systemservice.history.service.HistoryService.*;

/**
 * @author jenkin
 * @className HistoryController
 * @description TODO
 * @date 2021/2/1 17:12
 */
@RestController
@RequestMapping("/history")
@CrossOrigin
@Slf4j
@EnableErrorCatch()
@Api(tags = "四史答题相关接口")
public class HistoryController {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private TaskService taskService;

    private String USER_TASK_IS_RUNNING="task:user:running:";

    @Autowired
    private Redis redis;

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


    @GetMapping("/checkLogin")
    @ApiOperation("检查是否登录")
    public Response<OAuthService.UserLoginData> checkLogin(String random){
        Response<Response<OAuthService.UserLoginData>> comfirm = oAuthService.comfirm(random, true);
        return Response.ok(comfirm.getData().getData());
    }


    @GetMapping("/getUser")
    @ApiOperation("获取用户信息")
    public Response<HistoryService.UserInfo> getUserInfo(){
        Response<HistoryService.UserInfo> user=null;
        try {
            user = historyService.getUser();
        }catch (Exception e){
            throw new LscException(ExceptionEnum.QRCODE_LOGIN_ERROR_EXCEPTION);

        }

        if (user!=null&user.getCode().equals("0")) {
            return Response.ok(user.getData());
        }
        throw new LscException(ExceptionEnum.QRCODE_LOGIN_ERROR_EXCEPTION);
    }
    @GetMapping("/getGradeInfo")
    @ApiOperation("获取积分信息")
    public Response<HistoryService.Person> getGradeInfo(String activityId){
        if (StringUtils.isEmpty(activityId)) {
            activityId = ACTIVITY_ID;
        }
        Response<HistoryService.Person> grade =null;
        try {
            grade = historyService.grade(activityId);
        }catch (Exception e){
            throw new LscException(ExceptionEnum.QRCODE_LOGIN_ERROR_EXCEPTION);

        }
        if (grade!=null&&grade.getCode().equals("0")) {
            return Response.ok(grade.getData());
        }
        throw new LscException(ExceptionEnum.QRCODE_LOGIN_ERROR_EXCEPTION);
    }
    @GetMapping("/getQrCode")
    @ApiOperation("获取二维码")
    public Response<OAuthService.QrCode> getQrCode(){
        return oAuthService.getQrCode(UUID.randomUUID().toString().substring(0,10),true,false);
    }
    @GetMapping("/stopTask")
    @ApiOperation("停止任务")
    public Response stopTask(){
        redis.set("history:task:status:"+ShiroUtils.getUserCode(),"N");
        return Response.ok();
    }

    @GetMapping("/getUserTaskStatus")
    @ApiOperation("获取用户任务状态")
    public Response getUserTaskStatus(){
        return Response.ok(redis.get("history:task:status:" + ShiroUtils.getUserCode()));
    }

    @PostMapping("/startStudy")
    @ApiOperation("开始学习四史")
    public Response startStudy(@RequestBody TaskParam taskParam){

        if ("Y".equals(redis.get("history:task:status:" + ShiroUtils.getUserCode()))) {
            throw new LscException(ExceptionEnum.ERROR_START_EXCEPTION);
        }

        TaskService.LscTaskInfo<TaskParam> taskInfo = new TaskService.LscTaskInfo<>();
        AtomicReference<String> taskCodeRef = new AtomicReference<>();
        taskInfo.setTaskTitle("四史刷题");
        taskInfo.setTaskType("1");
        taskInfo.setTaskParam(taskParam);
        if (taskParam.getTaskCode()!=null) {
            taskService.startTask(new String[]{taskParam.getTaskCode()}, (task, user) -> {
                return runTask(task, user);
            });
        }else {
            taskService.createAndStartTask(taskInfo, (task, user) -> {
                taskCodeRef.set(task.getTaskCode());
                return runTask(task, user);
            });
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Response.ok(redis.get("history:task:status:" + ShiroUtils.getUserCode()));
    }

    private TaskService.TaskExecuteResult runTask(LscTaskDto task, String user) {
        Response<HistoryService.UserInfo> userInfo = historyService.getUser();
        String userId = userInfo.getData().getId();
        //当前任务处理的进度
        String task_process_key = "history:task:process:"+task.getTaskCode();
        //当前用户的积分
        String task_user_grade = "history:task:grade:"+userId;
        //当前任务正在处理的事情
        String activity_task_key = "history:task:activity"+task.getTaskCode();
        TaskService.TaskExecuteResult result = new TaskService.TaskExecuteResult();

        //清理积分
        redis.del(task_user_grade);

        String taskParamContent = task.getTaskParamContent();
        TaskParam taskParam  = JSON.parseObject(taskParamContent, TaskParam.class);
        Integer maxGrade = taskParam.getMaxGrade();

        //p判断当前是任务是否没有处理完（task_process_key是否有值并且大于0），如果没有那么需要获取已经处理了的编号
        Object o = redis.get(task_process_key);
        int process = -1;
        if (o!=null) {
            try {
                process = Integer.parseInt(String.valueOf(o));
            }catch (Exception e){
            }
        }
            //开始接着处理任务
        boolean b = dealTask(process, task_process_key, task_user_grade, activity_task_key, maxGrade, user,task.getTaskCode());
        if (b){
            task.setTaskStatus(TaskStatusEnum.COMPLETE_SUCCESS.getIntCode());
        }else{
            task.setTaskStatus(TaskStatusEnum.PASS_TASK.getIntCode());
        }
        return result;
    }

    private boolean dealTask(int process, String task_process_key, String task_user_grade, String activity_task_key, Integer maxGrade, String user,String taskCode) {
        redis.set("history:task:status:" + user,"Y");
        while (true) {
            QuestionsResponse questions = null;
            if (process >= 0) {
                questions = redis.getObject(activity_task_key, QuestionsResponse.class);
                log.info("有未完成的：{}",process);
            } else {
                questions = historyService.getQuestionList(ACTIVITY_ID, MODE_ID, WAY + "");
                redis.setObject(activity_task_key, questions);
                log.info("新的一轮");
                process = 0;
            }
            if (questions != null && questions.getQuestion_ids().size() > 0) {
                log.info("问题总数：{}",questions.getQuestion_ids().size());
                for (int i = process; i < questions.getQuestion_ids().size(); i++) {


                    Object o = questions.getQuestion_ids().get(i);
                    if (o != null) {
                        String code = String.valueOf(o);
                        try {
                            Response<Question> questionInfo = historyService.getQuestionInfo(ACTIVITY_ID, code, MODE_ID, WAY + "");
                            if (questionInfo != null && questionInfo.getData() != null) {
                                String answerCode = questionInfo.getData().getOptions().get(0).getCode();
                                HistoryService.AnswerParam answerParam = new HistoryService.AnswerParam();
                                answerParam.setActivity_id(ACTIVITY_ID);
                                answerParam.setMode_id(MODE_ID);
                                answerParam.setWay(WAY + "");
                                answerParam.setQuestion_id(code);
                                answerParam.setAnswer(Collections.singletonList(answerCode));
                                Answer answer = historyService.answer(answerParam).getData();
                                Thread.sleep(500);
                                if (answer.getCorrect() == null || !answer.getCorrect()) {
                                    answerParam.setAnswer(answer.getCorrect_ids());
                                    answer = historyService.answer(answerParam).getData();
                                    Thread.sleep(600);
                                    if (answer.getCorrect() != null && answer.getCorrect()) {
                                        redis.set(task_process_key, process + 1);
                                        Object grade = redis.get(task_user_grade);
                                        if (grade == null) {
                                            Response<Person> pg = historyService.grade(ACTIVITY_ID);
                                            Integer integral = pg.getData().getIntegral();
                                            grade = integral + "";
                                            redis.set(task_user_grade, grade);
                                        }
                                        int g = Integer.parseInt(String.valueOf(grade));
                                        redis.set(task_user_grade, g + 1);
                                        log.info("答题成功：{}  当前积分：{}", code, g + 1);
                                        sendGread(g + 1, taskCode,user);

                                        if (answer.getFinished() ||i==questions.getQuestion_ids().size()-1||maxGrade <= g + 1
                                                ||"N".equals(redis.get("history:task:status:" + user))) {
                                            Submit submit = new Submit();
                                            submit.setRace_code(questions.getRace_code());
                                            Response<SubmitEntiry> finish = historyService.finish(submit);
                                            Thread.sleep(3000);
                                            if (finish.getCode().equals("0")) {
                                                log.info("交卷成功, {} ",JSON.toJSONString(finish));

                                                redis.del(task_process_key, activity_task_key);
                                                if (maxGrade <= g + 1) {
                                                    redis.set("history:task:status:" + user,"N");
                                                    log.info("已到达阈值：max：：{}  current：：{}",maxGrade,g+1);
                                                    return true;
                                                }
                                                if("N".equals(redis.get("history:task:status:" + taskCode))){
                                                    log.info("手动停止：max：：{}  current：：{}",maxGrade,g+1);
                                                    return false;
                                                }
                                            }else{
                                                log.error("交卷失败");
                                            }
                                        }


                                    }
                                }
                            }else{
                                redis.del(task_process_key, activity_task_key);
                                redis.set("history:task:status:" + user,"N");
                                throw new LscException(ExceptionEnum.QRCODE_LOGIN_ERROR_EXCEPTION);
                            }
                        } catch (NumberFormatException|InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                    if (i==questions.getQuestion_ids().size()-1){
                        process=-1;
                    }
                }

            }
        }


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
        @ApiModelProperty("任务编号")
        private String taskCode;


    }


    private void sendGread(int grade,String taskCode,String user){
        TriggerVO triggerVO = new TriggerVO();
        triggerVO.setTriggerUsers(new String[]{user});
        triggerVO.setTriggerCode(MessageSender.TASK_SSXX_GRADE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskCode",taskCode);
        jsonObject.put("grade",grade+"");
        triggerVO.setTriggerMsg(JSON.toJSONString(jsonObject));
        MessageSender messageSender = MessageSender.getMessageSender(MessageSender.TASK_SSXX_GRADE);
        if (messageSender!=null) {
            messageSender.sendMessage(JSON.toJSONString(triggerVO),user);
        }
    }





}
