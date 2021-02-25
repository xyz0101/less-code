package com.jenkin.common.utils.demo;

import java.util.Arrays;
import java.util.Spliterator;

/**
 * @author jenkin
 * @className ShellSort
 * @description 希尔排序，是插入排序的变种，希尔排序定义一个基准希尔因子，默认为长度/2，根据这个希尔因子进行分组，然后进行组内排序，直至希尔因子为1
 * @date 2021/2/25 10:41
 */
public class ShellSort {

    public static void main(String[] args) {
        int[] arr = new int[]{4,2,1,4,6,3,2,4,5,2,6,5,5,0,9,7,88,7,9};
//        arr=new int[]{7,6,9,3,1,5,2,4};
        new ShellSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private void sort(int[] arr) {
        int base = arr.length/2;
        shellSort(base,arr);




    }

    private void shellSort(int base, int[] arr) {
         while(base>0){
             insertSort(arr,base);
             base = base/2;
         }
    }


    /**
     * 带base的希尔排序
     * @param arr
     */
    private void insertSort(int[] arr,int base){
        for (int i = 0; i < arr.length; i++) {
            for(int j = i+base;j< arr.length;j=j+base){
                if(arr[j]<=arr[i]){
                    int temp = arr[i];
                    arr[i]=arr[j];
                    arr[j]=temp;
                }
            }
        }
    }


}
