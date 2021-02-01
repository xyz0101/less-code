package com.jenkin.systemservice.examine_task.service;

import com.jenkin.common.entity.dtos.tasks.LscTaskDto;

@FunctionalInterface
public  interface FunctionExecute {
       TaskService.TaskExecuteResult execute(LscTaskDto taskDto, String user);
}
