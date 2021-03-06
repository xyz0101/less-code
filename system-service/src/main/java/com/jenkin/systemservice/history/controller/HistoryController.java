package com.jenkin.systemservice.history.controller;

import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jenkin.common.anno.EnableErrorCatch;
import com.jenkin.common.constant.HistroyConst;
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
import org.apache.tomcat.util.codec.binary.Base64;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.jenkin.common.constant.ThreadPoolConst.THREAD_HEADER;
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


    @Autowired
    private Redis redis;

    @GetMapping("/getToken")
    @ApiOperation("获取token")
    public Response<String> getToken(String uid){

    String token = getUserToken(uid);
    return Response.ok(token);
    }

    private String getUserToken(String uid) {
        if (StringUtils.isEmpty(uid)){
            throw new LscException(ExceptionEnum.ERROR_PARAM_EXCEPTION);

        }
        String tkey= HistroyConst.USER_TASK_TOKEN+uid;
        Object o = redis.get(tkey);
        if (o != null) {
            return String.valueOf(o);
        }
        try {
            String token = historyService.getToken(uid,
                    null, ACTIVITY_ID).getToken();
            redis.set(tkey,token);
            return token;
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
            redis.set(HistroyConst.USER_TASK_TOKEN_UID+ShiroUtils.getToken(),user.getData().getId());
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
        }else{
            log.info("grade 结果：{}",JSON.toJSONString(grade));
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
      String uid = getUserInfo().getData().getId();
       String retry=task_process_key+":retry";
        redis.set(retry,0);
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
                this.checkCode();
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
                                log.info("第一次回答 答案：{}",JSON.toJSONString(answer));
                                Thread.sleep(5);
                                if (answer!=null&&answer.getCorrect() == null || !answer.getCorrect()) {
                                    answerParam.setAnswer(answer.getCorrect_ids());
                                    answer = historyService.answer(answerParam).getData();
                                    if (answer==null) {
                                        continue;
                                    }
                                    log.info("第二次回答 答案：{}",JSON.toJSONString(answer));
                                    Thread.sleep(5);
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
                                            Thread.sleep(100);
                                            if (finish.getCode().equals("0")) {
                                                log.info("{} 交卷成功, {} ",uid,JSON.toJSONString(finish));

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
                                                log.error("交卷失败 原因：{}",JSON.toJSONString(finish));
                                            }
                                        }


                                    }
                                }else{
                                    log.warn("答案有误");
                                }
                            }else{
                                Integer count = redis.getObject(retry, Integer.class);
                                if (count >10) {
                                    redis.set(retry,0);
                                    redis.del(task_process_key, activity_task_key);
                                    redis.set("history:task:status:" + user, "N");
                                    log.info("用户 {} token失效，{}",uid+"("+user+")",JSON.toJSONString(questionInfo));
                                    throw new LscException(ExceptionEnum.QRCODE_LOGIN_ERROR_EXCEPTION);
                                }else{
                                    log.info("token失效 重试 第 {} 次",count+1);
                                    String token = getUserToken(uid);
                                    THREAD_HEADER.get().put("authorization","Bearer "+token);
                                    redis.set(retry,count+1);
                                }
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
    @GetMapping("/test")
    public Response test(){
        checkCode();
        return Response.ok();
    }
    private void checkCode() {

        String code = UUID.randomUUID().toString().substring(0,4);
        Response<MyPublicKey> publicKey = historyService.getPublicKey();
        String public_key = publicKey.getData().getPublic_key();
        public_key=public_key.replaceAll("\n","");
//        public_key=public_key.replaceAll("-----BEGIN PUBLIC KEY-----","-----BEGIN PUBLIC KEY-----\n");
        public_key=public_key.replaceAll("-----BEGIN PUBLIC KEY-----","");
//        public_key=public_key.replaceAll("-----END PUBLIC KEY-----","\n-----END PUBLIC KEY-----");
        public_key=public_key.replaceAll("-----END PUBLIC KEY-----","");
        System.out.println(public_key);
        try {
            byte[] encrypt = encryptByPublicKey(code.getBytes(),public_key);
            String codeStr = new String(Base64.encodeBase64(encrypt));
            log.info(codeStr);
            CodeParam codeParam = new CodeParam();
            codeParam.setActivity_id(ACTIVITY_ID);
            codeParam.setCode(codeStr);
            codeParam.setMode_id(MODE_ID);
            codeParam.setWay(WAY+"");
            historyService.saveCode(codeParam);
            CheckStatus checkStatus = historyService.checkCode(codeParam);
            log.info("校验结果 ：{}",JSON.toJSONString(checkStatus));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 256) {
                cache = cipher.doFinal(data, offSet, 256);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 256;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;

    }
    //加密，需要公钥
    public byte[] encrypt(byte[] ptext, PublicKey pbkey) throws Exception {
        // 获取公钥及参数e,n
        RSAPublicKey pbk = (RSAPublicKey)pbkey;
        BigInteger e = pbk.getPublicExponent();
        BigInteger n = pbk.getModulus();
        // 获取明文m
        BigInteger m = new BigInteger(ptext);
        // 计算密文c
        BigInteger c = m.modPow(e, n);
        return c.toByteArray();
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
