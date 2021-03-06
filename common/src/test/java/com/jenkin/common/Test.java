package com.jenkin.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jenkin
 * @className Test
 * @description TODO
 * @date 2021/3/1 14:05
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {

        ReentrantLock lock = new ReentrantLock();

        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

            for (int i = 0; i < 10; i++) {
                final String index =i+"";
                executorService.execute(()->{
                    threadLocal.set(index);
                });
            }

        Thread.sleep(1000);
        String s = threadLocal.get();

        System.out.println(s);
    }
}
