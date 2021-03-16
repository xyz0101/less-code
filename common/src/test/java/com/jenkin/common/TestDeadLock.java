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
public class TestDeadLock {
    public static void main(String[] args) throws InterruptedException {

        ReentrantLock lock = new ReentrantLock();
        ReentrantLock lock1 = new ReentrantLock();
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        ExecutorService executorService = Executors.newFixedThreadPool(100);

            for (int i = 0; i < 10; i++) {
                final String index =i+"";
                executorService.execute(()->{
                    double v = Math.random() * 10;
                    if (v>5) {

                        lock1.lock();
//                        synchronized (lock) {
                            System.out.println(Thread.currentThread().getId() + " 获取锁1");
                            try {
                                Thread.sleep(2000);
                        lock.lock();
//                                synchronized (lock1) {
                                    try {
                                        System.out.println(Thread.currentThread().getId() + " 获取锁2");
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
//                                }
                        lock.unlock();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
//                        }
                    lock1.unlock();
                    }else{


//                        synchronized (lock1) {
                            System.out.println(Thread.currentThread().getId() + " 获取锁1");
                            try {
                                lock.lock();
                                Thread.sleep(2000);

//                                synchronized (lock) {
                                    try {
                                        lock1.lock();
                                        System.out.println(Thread.currentThread().getId() + " 获取锁2");
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }finally {
                                        lock1.unlock();
                                    }
//                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }finally {
                                lock.unlock();

                            }
                        }
//                    }


                });
            }

        Thread.sleep(1000);
        String s = threadLocal.get();

        System.out.println(s);
    }
}
