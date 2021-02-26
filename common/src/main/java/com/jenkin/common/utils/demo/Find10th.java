package com.jenkin.common.utils.demo;

import io.swagger.models.auth.In;

import java.util.*;

/**
 * @author jenkin
 * @className Find10th
 * @description 找出数组里面第十大的数据
 * @date 2021/2/20 10:16
 */
public class Find10th {
    public static void main(String[] args) {
        System.out.println(new Find10th().find10th(new int[]{1,5,5,6,7,4,3,1,3,4,5,5,6,5,6,5,6,7,4,3,1,3,4,5,7,4,5,6,7,
                4,3,1,3,4,5,3,1,3,4,5,5,6,7,4,3,1,3,4,5,7,4,3,5,6,7,4,3,1,3,4,5,1,5,6,7,4,3,1,3,4,5,3,5,6,7,4,3,1,3,4,5,
                4,5,5,6,7,4,3,1,3,4,5,6,7,8,9,10,22,33,44,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,5,6,7,4,3,1,3,4,5
                ,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5
                ,5,6,7,4,3,1,3,4,5,5,6,7,4,5,6,7,4,3,1,3,4,5,3,1,5,6,7,4,3,1,3,4,5,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3
                ,4,5,5,6,5,6,7,4,3,1,3,4,5,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5
                ,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5
                ,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5
                ,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5,5,6,7,4,3,1,3,4,5}));
    }

    /**
     * 找出第十大的数，不含重复
     * @param ints
     * @return
     */
    private int find10th(int[] ints) {

        TreeSet<Integer> ts = new TreeSet<>(Comparator.comparingInt(o -> o));
        for (int anInt : ints) {
            ts.add(anInt);
        }
        Iterator<Integer> iterator = ts.iterator();
        int i=0;
        while(iterator.hasNext()){
            Integer next = iterator.next();
            if (i==9) {
                return next;
            }
            i++;
        }
        return -1;
    }

    /**
     * 找出第4最大的数字，含重复
     * @param ints
     * @return
     */
    private int findMax4Big(int[] ints) {

        PriorityQueue<Integer> ts = new PriorityQueue<>((o1, o2) -> Integer.compare(o2 - o1, 0));
        for (int anInt : ints) {
            ts.offer(anInt);
        }
        int i=0;
        while(ts.peek()!=null){
            Integer next = ts.poll();
            if (i==4-1) {
                return next;
            }
            i++;
        }
        return -1;
    }
}
