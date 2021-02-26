package com.jenkin.common.utils.demo;

import java.util.Arrays;

/**
 * @author jenkin
 * @className MergeSort
 * @description 归并排序
 * @date 2021/2/26 17:16
 */
public class MergeSort {


    public static void main(String[] args) {
        int[] arr = new int[]{4,2,1,4,6,3,2,4,5,2,6,5,5,0,9,7,88,7,9};
        new MergeSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private void sort(int[] arr) {

        //先写归并



        int start = 0,end = arr.length-1;

        int i = start,j=end;
        int mid = i/j;





    }

    private void mergeSort(int[] arr ) {

    }


}
