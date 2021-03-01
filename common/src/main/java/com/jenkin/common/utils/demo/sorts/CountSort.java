package com.jenkin.common.utils.demo.sorts;

import java.util.Arrays;

/**
 * @author ：jenkin
 * @date ：Created at 2021/2/28 11:34
 * @description：计数排序
 * @modified By：
 * @version: 1.0
 */
public class CountSort {

    public static void main(String[] args) {
        int[] arr = new int[]{4,2,1,4,6,3,2,4,5,2,6,5,5,0,9,7,88,7,9};


        new ChooseSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     *  计数排序，思路是这样
     *  1、首先定义一个长度为 max-min +1 的数组，称之为计数数组
     *  2、遍历原始数组，当前元素-min为技术数组的索引，在该位置上元素+1
     *  3、遍历计数数组，（min+每个元素的索引代表原始数字），计数数组里面的值代表改数字出现的次数
     * @param arr
     */
    public void sort(int[] arr) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int num : arr) {
            max=Math.max(num,max);
            min=Math.min(num,min);
        }
        //定义计数数组
        int[] countArr = new int[(max-min)+1];
        //遍历原始数组
        for (int num : arr) {
            countArr[num-min]++;
        }
        //遍历计数数组
        int k=0;
        for (int i = 0; i < countArr.length; i++) {
            int num = countArr[i];
            if (num>0) {
                for (int j = 0; j < num; j++) {
                    arr[k++]=i+1;
                }
            }
        }
    }
}
