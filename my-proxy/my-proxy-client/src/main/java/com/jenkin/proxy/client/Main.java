package com.jenkin.proxy.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/28 21:24
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Main {
    static Object o = new Object();
    private static Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    public static void main(String[] args) throws InterruptedException {

        synchronized (o){
            logger.info("开始进入锁");
            new Thread(()->{
                try {
                    Thread.sleep(3000);
                    synchronized (o){
                        o.notify();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            o.wait();
            logger.info("锁被唤醒");
        }

    }
}
