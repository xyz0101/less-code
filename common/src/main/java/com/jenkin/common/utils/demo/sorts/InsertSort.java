package com.jenkin.common.utils.demo.sorts;

import java.util.Arrays;

/**
 * @author jenkin
 * @className InsertSort
 * @description TODO
 * @date 2021/2/23 17:42
 */
public class InsertSort {
    public static void main(String[] args) {
        int[] arr = new int[]{4,2,1,4,6,3,2,4,5,2,6,5,5,0,9,7,88,7,9};


        new InsertSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 插入排序，假定一个有序的数组（比如最左侧），每次从后面找到比它小的数和他交换位置
     * @param arr
     */
    private void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {

                if( arr[j]<arr[j-1]){
                    int temp = arr[j-1];
                    arr[j-1]=arr[j];
                    arr[j]=temp;
                }
            }
        }
    }
}
