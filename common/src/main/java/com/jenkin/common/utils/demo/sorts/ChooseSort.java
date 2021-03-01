package com.jenkin.common.utils.demo.sorts;

import java.util.Arrays;

/**
 * @author jenkin
 * @className ChooseSort
 * @description TODO
 * @date 2021/2/24 16:39
 */
public class ChooseSort {
    public static void main(String[] args) {
        int[] arr = new int[]{4,2,1,4,6,3,2,4,5,2,6,5,5,0,9,7,88,7,9};


        new ChooseSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 选择排序，每次从数组中选择一个最小的放在最左侧
     * @param arr
     */
    public void sort(int[] arr) {


        for (int i = 0; i < arr.length; i++) {
            int index =i;
            int temp = arr[i];
            int min = temp;
            for (int j = i; j <arr.length; j++) {
                if(arr[j]<=min){
                    min=arr[j];
                    index=j;
                }
            }
            arr[i]=min;
            arr[index]=temp;

        }
    }
}
