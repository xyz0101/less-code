package com.jenkin.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/13 15:42
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class TestOOM {


    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            while(true){
                try {

                    List<String> res = new ArrayList<>(900000) ;
                    System.out.println("1 分配成功");
                    Thread.sleep((long) (Math.random()*10));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(()->{
            while(true){
                try {

                    List<Integer> res = new ArrayList<>((int)(700000*Math.random())) ;
                    System.out.println("2 分配成功");
                    Thread.sleep((long) (Math.random()*100));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }



}
