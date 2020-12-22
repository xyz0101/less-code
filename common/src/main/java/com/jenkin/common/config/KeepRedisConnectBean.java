package com.jenkin.common.config;

import com.jenkin.common.utils.Redis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @author jenkin
 * @className KeepRedisConnectUtils
 * @description TODO
 * @date 2020/12/21 17:53
 */
@Component
@Slf4j
public class KeepRedisConnectBean {

    @Autowired
    Redis redis;

    private static final ScheduledThreadPoolExecutor SINGLE_SCHEDULED_EXECUTOR =  new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("jenkin schedule keep redis connect");
            return thread;
        }
    });

    public  void run(){
        SINGLE_SCHEDULED_EXECUTOR.schedule(()->{
            log.info("keep redis connect");
            try {
                redis.set("SINGLE_SCHEDULED_EXECUTOR", "jenkin schedule keep redis connect");
            }catch (Exception e){
                log.error(e.getMessage());
            }finally {
                run();
            }
        },60, TimeUnit.SECONDS);
    }
    @PostConstruct
    public void postConstruct(){
        log.info(" 系统初始化 后台执行 keep redis connect");
        this.run();
    }
}
