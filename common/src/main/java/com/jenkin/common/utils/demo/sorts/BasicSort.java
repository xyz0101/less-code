package com.jenkin.common.utils.demo.sorts;

import cn.hutool.core.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/1 20:21
 * @description：基数排序
 * @modified By：
 * @version: 1.0
 */
public class BasicSort {

    public static void main(String[] args) {
        int[] arr = new int[]{4,2,1,4,6,3,2,4,5,2,6,5,5,0,9,7,88,7,9};


        new BasicSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 在计数排序里面我们把每个每个元素放到对应的桶里面然后再取出来元素是有序的
     * 而在基数排序里面我们根据元素的位数来进行一个分类，然后进行多轮的计数排序
     * @param arr
     */
    public void sort(int[] arr) {
        int max = ArrayUtil.max(arr);
        max = String.valueOf(max).length() ;
        max= (int) Math.pow(10,max);
        int k=1;
        while(k<max){


            //定义一个桶，由于每一位数的大小为10，所以定义10个桶
            List<List<Integer>> bucket = new ArrayList<>(10);
            for (int i = 0; i < 10; i++) {
                bucket.add(new ArrayList<>());
            }
            for (int i : arr) {
                int index = (i/k)%10;

                bucket.get(index).add(i);
            }
            int i=0;
            for (List<Integer> item : bucket) {
                for (Integer val : item) {
                    arr[i++]=val;
                }
            }
            System.out.println(Arrays.toString(arr));
            k=k*10;
        }


    }
}
