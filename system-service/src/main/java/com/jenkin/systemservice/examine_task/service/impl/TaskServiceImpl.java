package com.jenkin.systemservice.examine_task.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.github.dozermapper.core.Mapper;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.config.MyUpdateWrapper;
import com.jenkin.common.constant.ThreadPoolConst;
import com.jenkin.common.entity.dtos.tasks.LscTaskDto;
import com.jenkin.common.entity.pos.tasks.LscTaskPo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.tasks.LscTaskQo;
import com.jenkin.common.entity.vos.tasks.TriggerVO;
import com.jenkin.common.enums.TaskStatusEnum;
import com.jenkin.common.shiro.utils.ShiroUtils;
import com.jenkin.common.utils.DateUtils;
import com.jenkin.common.utils.IDGenerator;
import com.jenkin.common.utils.NotEmptyUtils;
import com.jenkin.common.utils.SimpleQuery;
import com.jenkin.systemservice.examine_task.dao.TaskMapper;
import com.jenkin.systemservice.examine_task.service.FunctionExecute;
import com.jenkin.systemservice.examine_task.service.TaskService;
import com.jenkin.systemservice.examine_task.socket.MessageSender;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

import static com.jenkin.common.constant.ThreadPoolConst.TASK_THREADS;
import static com.jenkin.common.constant.ThreadPoolConst.THREAD_TOKEN;
import static com.jenkin.common.enums.TaskStatusEnum.EXAMINING;
import static com.jenkin.common.enums.TaskStatusEnum.UN_START;


/**
 * @author jenkin
 * @className LabelServiceImpl
 * @description TODO
 * @date 2020/6/28 15:17
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, LscTaskPo> implements TaskService {
    @Autowired
    private Mapper mapper;

    /**
     * 手动开启事物
     */
    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    TransactionDefinition transactionDefinition;

    private Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Override
    public TaskExecuteResult getSingleTaskResultByCode(String taskCode) {
        if (StringUtils.isEmpty(taskCode)) {
            return null;
        }
        MyQueryWrapper<LscTaskPo> queryWrapper = new MyQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasLength(taskCode),LscTaskPo.Fields.taskCode,taskCode);
        LscTaskPo LscTaskPo = getOne(queryWrapper);
        if (LscTaskPo==null||LscTaskPo.getTaskResult()==null) {
            return null;
        }
        return JSON.parseObject(LscTaskPo.getTaskResult(),TaskExecuteResult.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startTask(String[] codes,FunctionExecute execute) {
        if (ArrayUtil.isEmpty(codes)){
            return;
        }
        //修改状态为等待中
        List<String> updateCodes = updateStatusWaiting(codes, false);
        sendTaskStatus(ShiroUtils.getUserEntity().getUserName(),updateCodes, MessageSender.TASK_STATUS, TaskStatusEnum.WAITING);
        //执行任务
        runTask(updateCodes,execute);
    }

    /**
     * 执行任务
     * @param codes
     */
    private void runTask(final List<String> codes, FunctionExecute execute) {
        String user = ShiroUtils.getUserEntity().getUserName();
        String token = ShiroUtils.getToken();

        for (String code : codes) {
            ThreadPoolConst.EXAM_TASK_JOBS_EXECUTORS.execute(() -> {
                Thread thread = TASK_THREADS.get(code);
                if (thread !=null) {
                    Thread.State state = thread.getState();
                    logger.info(state.toString());
                    thread.interrupt();
                    TASK_THREADS.remove(code);
                }
                TASK_THREADS.put(code,Thread.currentThread());
                THREAD_TOKEN.set(token);
                try {
                    logger.info("任务token{}", token);

                    LscTaskDto task = getSingleTaskByCode(code);
                    if (task == null) {
                        return;
                    }

                    //只运行等待状态的
                    if (task.getTaskStatus().intValue() == TaskStatusEnum.WAITING.getIntCode()) {
                        task.setTaskStatus(TaskStatusEnum.RUNNING.getIntCode());
                        task.setTaskStartTime(LocalDateTime.now());
                        this.updateTask(null, task, user);
                        dealTask(task,user,execute);
                    }
                }finally {
                    THREAD_TOKEN.remove();
                    TASK_THREADS.remove(code);
                }
            });
        }



    }

    /**
     * 处理任务
     * @param task
     * @param user
     * @param execute
     */
    private void dealTask(LscTaskDto task, String user, FunctionExecute execute) {
        Long b = System.currentTimeMillis();
        TaskExecuteResult taskExecuteResult = null;
        String taskContent = task.getTaskParamContent();
        try {

            if (NotEmptyUtils.allNotEmpty(task, taskContent)) {
                taskExecuteResult= execute.execute(task,user);
            } else {
                task.setTaskStatus(TaskStatusEnum.COMPLETE_SUCCESS.getIntCode());
            }
            task.setTaskEndTime(LocalDateTime.now());

        } catch (Exception e) {
             JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg",ExceptionUtil.stacktraceToString(e));
            taskExecuteResult = new TaskExecuteResult();
            taskExecuteResult.setResult(jsonObject);

            task.setTaskStatus(TaskStatusEnum.COMPLETE_FAIL.getIntCode());
            e.printStackTrace();
        } finally {
            task.setTaskCost((int) (System.currentTimeMillis() - b));
            task.setTaskResult(JSON.toJSONString(taskExecuteResult));
            task.setTaskEndTime(LocalDateTime.now());
            //根据结果更新当前任务
            updateTask(taskExecuteResult, task, user);
            this.sendTaskStatus(user,  MessageSender.TASK_STATUS,task);
        }
    }

    @Override
    public LscTaskDto getSingleTaskByCode(String code) {
        MyQueryWrapper<LscTaskPo> queryWrapper = new MyQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasLength(code),LscTaskPo.Fields.taskCode,code);
        LscTaskPo LscTaskPo = getOne(queryWrapper);
        if (LscTaskPo==null ) {
            return null;
        }
        return mapper.map(LscTaskPo,LscTaskDto.class);
    }





    private void cancelAll(LscTaskDto task,String user) {
        logger.info("中断响应，全部取消");
        task.setTaskStatus(TaskStatusEnum.CANCEL.getIntCode());
        updateTask(null, task, user);
        this.sendTaskStatus(user,Collections.singletonList(task.getTaskCode()), MessageSender.TASK_STATUS,TaskStatusEnum.CANCEL);
    }

    /**
     * 触发任务状态变化
     * @param user
     * @param triggerCode
     * @param taskPO
     */
    private synchronized void sendTaskStatus(String user , String triggerCode ,LscTaskPo taskPO) {
        TriggerVO triggerVO = new TriggerVO();
        triggerVO.setTriggerUsers(new String[]{user});
        triggerVO.setTriggerCode(triggerCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskCodes",JSON.toJSONString(Collections.singletonList(taskPO.getTaskCode())));
        TaskStatusEnum statusEnum = TaskStatusEnum.getById(taskPO.getTaskStatus());
        jsonObject.put("statusIntCode",statusEnum.getIntCode());
        jsonObject.put("statusStrCode",statusEnum.getCode());
        jsonObject.put("statusName",statusEnum.getName());
        jsonObject.put("startTime", DateUtils.parseTime(taskPO.getTaskStartTime()));
        jsonObject.put("endTime",DateUtils.parseTime(taskPO.getTaskEndTime()));
        jsonObject.put("taskCost",taskPO.getTaskCost());
        triggerVO.setTriggerMsg(JSON.toJSONString(jsonObject));
        String msg = JSON.toJSONString(triggerVO);

        MessageSender messageSender = MessageSender.getMessageSender(triggerCode);
        if (messageSender!=null) {
            messageSender.sendMessage(msg,user);
        }
    }

    /**
     * 触发任务状态变化
     * @param user
     * @param taskCodes
     * @param triggerCode
     * @param statusEnum
     */
    private synchronized void sendTaskStatus(String user,List<String> taskCodes, String triggerCode,  TaskStatusEnum statusEnum) {
        TriggerVO triggerVO = new TriggerVO();
        triggerVO.setTriggerUsers(new String[]{user});
        triggerVO.setTriggerCode(triggerCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskCodes",JSON.toJSONString(taskCodes));
        jsonObject.put("statusIntCode",statusEnum.getIntCode());
        jsonObject.put("statusStrCode",statusEnum.getCode());
        jsonObject.put("statusName",statusEnum.getName());
        triggerVO.setTriggerMsg(JSON.toJSONString(jsonObject));
        String msg = JSON.toJSONString(triggerVO);

        MessageSender messageSender = MessageSender.getMessageSender(triggerCode);
        if (messageSender!=null) {
            messageSender.sendMessage(msg,user);
        }
    }

    @Override
    public  void createTask(LscTaskInfo taskInfo ) {
        LscTaskPo taskPO = new LscTaskPo();
        taskPO.setTaskCode(IDGenerator.newNo("TASK"));
        taskPO.setTaskParamContent(JSON.toJSONString(taskInfo.getTaskParam()));
        taskPO.setTaskTitle(taskInfo.getTaskTitle());
        taskPO.setTaskType(taskInfo.getTaskType());
        taskPO.setTaskUser(ShiroUtils.getUserEntity().getUserName());
        save(taskPO);
    }

    /**
     * 创建一个任务
     *
     * @param taskInfo
     */
    @Override
    public void createAndStartTask(LscTaskInfo taskInfo,FunctionExecute execute) {
        LscTaskPo taskPO = new LscTaskPo();
        taskPO.setTaskCode(IDGenerator.newNo("TASK"));
        taskPO.setTaskParamContent(JSON.toJSONString(taskInfo.getTaskParam()));
        taskPO.setTaskTitle(taskInfo.getTaskTitle());
        taskPO.setTaskType(taskInfo.getTaskType());
        taskPO.setTaskUser(ShiroUtils.getUserEntity().getUserName());
        save(taskPO);
        startTask(new String[]{taskPO.getTaskCode()},execute);
    }

    private void updateTask(TaskExecuteResult taskExecuteResult, LscTaskDto task, String user) {
        if (taskExecuteResult!=null) {
            task.setTaskResult(JSON.toJSONString(taskExecuteResult));
        }
        LscTaskPo taskPO = mapper.map(task,LscTaskPo.class);
        updateById(taskPO);

    }

    @Override
    public void cancelTask(String[] codes) {
        if (ArrayUtil.isEmpty(codes)) {
            return;
        }
        MyUpdateWrapper<LscTaskPo> updateWrapper = new MyUpdateWrapper<>();
        updateWrapper.in(LscTaskPo.Fields.taskCode,codes);
        updateWrapper.le(LscTaskPo.Fields.taskStatus, EXAMINING.getIntCode());
        updateWrapper.set(LscTaskPo.Fields.taskStatus, TaskStatusEnum.CANCEL.getIntCode());
        update(updateWrapper);

        synchronized (TASK_THREADS) {
            logger.info("中断当前任务 {}",Arrays.toString(codes));
            for (String code : codes) {
                Thread thread = TASK_THREADS.get(code);
                if (thread != null) {
                    thread.interrupt();
                }
            }
        }

    }

    @Override
    public Page<LscTaskDto> listTasks(BaseQo<LscTaskQo> baseQO) {
        LscTaskQo data = baseQO.getData();

        MyQueryWrapper<LscTaskPo> queryWrapper = new MyQueryWrapper<>();
        if (data!=null) {
            queryWrapper.eq(NotEmptyUtils.allNotEmpty(data,data.getTaskStatus()),LscTaskPo.Fields.taskStatus,data.getTaskStatus());
            queryWrapper.eq(NotEmptyUtils.allNotEmpty(data,data.getTaskType()),LscTaskPo.Fields.taskType,data.getTaskType());
        }
        Page<LscTaskPo> page = new Page<>(baseQO.getPage(),baseQO.getPageSize());
        Page<LscTaskPo> result = page(page, queryWrapper);
        return SimpleQuery.page(result, LscTaskDto.class);
    }

    /**
     * 修改状态为等待中
     * @param codes
     * @param isRestart 是否是重新启动，如果是重新启动那么就不需要条件，否则就是需要只修改状态为UN_START的数据
     */
    private List<String> updateStatusWaiting(String[] codes,boolean isRestart) {
        MyQueryWrapper<LscTaskPo> queryWrapper = new MyQueryWrapper<>();
        queryWrapper.in(LscTaskPo.Fields.taskCode,codes);
        queryWrapper.eq(!isRestart,LscTaskPo.Fields.taskStatus,UN_START.getIntCode());
        List<LscTaskPo> list = list(queryWrapper);
        MyUpdateWrapper<LscTaskPo> updateWrapper = new MyUpdateWrapper<>();
        updateWrapper.in(LscTaskPo.Fields.taskCode,codes);
        updateWrapper.eq(!isRestart,LscTaskPo.Fields.taskStatus,UN_START.getIntCode());
        updateWrapper.set(LscTaskPo.Fields.taskStatus, TaskStatusEnum.WAITING.getIntCode());
        updateWrapper.set(LscTaskPo.Fields.taskStartTime,null);
        updateWrapper.set(LscTaskPo.Fields.taskEndTime,null);
        updateWrapper.set(LscTaskPo.Fields.taskCost,null);
        update(updateWrapper);
        List<String> updatedCodes = new ArrayList<>();
        list.forEach(item->updatedCodes.add(item.getTaskCode()));
        return updatedCodes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reStartTask(String[] codes,FunctionExecute execute) {
        if (ArrayUtil.isEmpty(codes)) {
            return;
        }
        //修改状态为等待中
        List<String> updateCodes = updateStatusWaiting(codes, true);
        sendTaskStatus(ShiroUtils.getUserEntity().getUserName(),updateCodes, MessageSender.TASK_STATUS,TaskStatusEnum.WAITING);
        //执行任务
        runTask(updateCodes,execute);
    }

    @Override
    public void cancelAllTask() {
        String currentUserName = ShiroUtils.getUserEntity().getUserName();
        MyQueryWrapper<LscTaskPo> queryWrapper = new MyQueryWrapper<>();
        queryWrapper.eq(LscTaskPo.Fields.taskUser,currentUserName);
        queryWrapper.between(LscTaskPo.Fields.taskStatus, UN_START.getIntCode(), EXAMINING.getIntCode());
        List<LscTaskPo> list = list(queryWrapper);
        List<String> codes = new ArrayList<>();
        for (LscTaskPo taskPO : list) {
            codes.add(taskPO.getTaskCode());
        }
        String[] codeArr = new String[codes.size()];
        codes.toArray(codeArr);
        cancelTask(codeArr);
    }

    @Override
    public void deleteTask(String[] codes) {
        if (ArrayUtil.isEmpty(codes)) {
            return;
        }
        MyQueryWrapper<LscTaskPo> queryWrapper = new MyQueryWrapper<>();
        queryWrapper.in(LscTaskPo.Fields.taskCode,codes).eq(LscTaskPo.Fields.taskUser,ShiroUtils.getUserEntity().getUserName());
        remove(queryWrapper);
    }



}
