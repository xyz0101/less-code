package com.jenkin.common.utils.demo;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jenkin
 * @className TestDoubleCheckLock
 * @description TODO
 * @date 2021/3/9 15:37
 */
public class TestDoubleCheckLock {
    private static MyObject myObject = null;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100000);

        System.out.println("--------------");
        CyclicBarrier cyclicBarrier = new CyclicBarrier(100000);
        for (int i = 100000; i > 0; i--) {
            executorService.submit(()->{
                try {
                    cyclicBarrier.await();
                    int v =TestDoubleCheckLock.getInstance().val;
                }catch (Exception e ){
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            });
        }

    }



    public static MyObject getInstance(){
        if(myObject==null){
            synchronized (MyObject.class){
                if(myObject==null) {
                    myObject = new MyObject();
                }
            }
        }
        return myObject;
    }


    static class MyObject{
        int val = 9;
    }
}

