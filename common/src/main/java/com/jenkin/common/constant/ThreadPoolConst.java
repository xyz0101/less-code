package com.jenkin.common.constant;

import cn.hutool.core.thread.ThreadFactoryBuilder;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author jenkin
 * @className ThreadPoolConst
 * @description 线程池常量
 * @date 2020/9/22 16:16
 */
public class ThreadPoolConst {

    public static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 异步任务的线程，核心为CPU个数，最大4倍，超过的线程允许空闲存活5分钟，阻塞队列1000个，拒绝策略让主线程执行
     */
    public static final ThreadPoolExecutor ASYNC_JOBS_EXECUTORS = new ThreadPoolExecutor(CPU_NUM,2*CPU_NUM,
            5, TimeUnit.MINUTES,new ArrayBlockingQueue<>(1000),
            ThreadFactoryBuilder.create().setNamePrefix("通知线程").build(),new ThreadPoolExecutor.CallerRunsPolicy());
    /**
     * 审核任务的线程，核心为2倍CPU个数，最大4倍，超过的线程允许空闲存活5分钟，阻塞队列1000个，拒绝策略抛异常
     */
    public static final ThreadPoolExecutor EXAM_TASK_JOBS_EXECUTORS = new ThreadPoolExecutor(20*CPU_NUM,40*CPU_NUM,
            5, TimeUnit.MINUTES,new ArrayBlockingQueue<>(100000),
            ThreadFactoryBuilder.create().setNamePrefix("刷题线程").build() ,new ThreadPoolExecutor.AbortPolicy());
    /**
     * token线程变量
     */
    public static final ThreadLocal<String> THREAD_TOKEN = new ThreadLocal<>();
    /**
     * header 线程变量
     */
    public static final ThreadLocal<Map<String,String>> THREAD_HEADER = new ThreadLocal<>();
    /**
     * 正在运行的任务的线程
     */
    public static final ConcurrentHashMap<String,Thread> TASK_THREADS = new ConcurrentHashMap<>();


}
