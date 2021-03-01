package com.jenkin.common.utils.demo.sorts;

import java.util.Arrays;

/**
 * @author jenkin
 * @className MapPao
 * @description TODO
 * @date 2021/2/23 17:33
 */
public class MaoPao {

    public static void main(String[] args) {
        int[] arr = new int[]{4,2,1,4,6,3,2,4,5,2,6,5,5,0,9,7,88,7,9};
        new MaoPao().maopao(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 冒泡排序，两两比较，只要比他小就交换位置
     * @param arr
     */
    public void maopao(int[] arr) {

        for (int i = 0; i < arr.length-1; i++) {
            for (int j = 0; j < arr.length-1; j++) {
                if(arr[j]>arr[j+1]){
                    int temp = arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }
}
