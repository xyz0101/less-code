package com.jenkin.common.utils.demo.sorts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2021/2/28 10:43
 * @description：桶排序
 * @modified By：
 * @version: 1.0
 */
public class BucketSort {

    public static void main(String[] args) {
        int[] arr = new int[]{24,2,1,4,16,23,22,14,15,2,26,25,15,10,19,7,18,27,19};


        new BucketSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 桶排序的思想，首先确定N个有序的桶，每一个桶有自己的数据范围
     * 把数据放到指定的桶里面，然后在桶内使用排序，让桶内数据变得有序
     * 再把桶的数据拼接起来就是已经排好序的数组
     * @param arr
     */
    public void sort(int[] arr) {

        //确定桶的数量（找出数组里面的最大最小值，然后除以数组的长度+1（ ((max-min)/arr.length) +1 ）。当然痛的数目确定不止这一种方法）
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int num : arr) {
            max=Math.max(num,max);
            min=Math.min(num,min);
        }
        int bucketsLength = ((max-min)/arr.length)+1;
        //定义桶
        List<List<Integer>> buckets = new ArrayList<>(bucketsLength);
        for (int i = 0; i < bucketsLength; i++) {
            buckets.add(new ArrayList<>());
        }
        //确定了桶的数量之后就可以确定每个桶的数据范围，例如范围是20，那么第一个桶的数据范围 [min,min+20)，第二个桶 [min+20,min+40) ....
        int bucketRange = (max-min)/bucketsLength;

        //把数据放在桶里面
        for (int num : arr) {
            //计算对应的桶的索引
            int index = (num/bucketRange+((num%bucketRange)!=0?1:0))-1;
            if(num==min){
                buckets.get(0).add(num);
            }else if (num==max){
                buckets.get(buckets.size()-1).add(num);
            }else{
                buckets.get(index).add(num);
            }
        }
        //对桶内排序
        for (List<Integer> bucket : buckets) {
            Collections.sort(bucket);
        }
        //把排序好的数据放入数组
        int i =0;
        for (List<Integer> bucket : buckets) {
            for (Integer integer : bucket) {
                arr[i] = integer;
                i++;
            }
        }
    }
}
