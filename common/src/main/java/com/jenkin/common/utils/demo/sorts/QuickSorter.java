package com.jenkin.common.utils.demo.sorts;

import java.util.Arrays;

/**
 * @author ：jenkin
 * @date ：Created at 2021/2/24 20:34
 * @description：快速排序
 * @modified By：
 * @version: 1.0
 */
public class QuickSorter {
    public static void main(String[] args) {
        int[] arr = new int[]{2,5,3,2,1,3,2,44,56,77,33,343,43,23,353,324,2341,22,34,34,21,23,1,434,34,3322,12};
//        int[] arr = new int[]{1,2,3,3,2,1};
        new QuickSorter().sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public void sort(int[] arr) {
        int start = 0;
        int end = arr.length-1;
        quickSort(arr,start,end);

    }

    private void quickSort(int[] arr, int start, int end) {

        if (end<=start) {
            return;
        }
        //当前的基数，每次都取第一个
        int base =arr[start];
        int i=start,j=end;
        //一次循环之后应该是基数左边都是比他小的数据，右边都是比他大的数据
        //跳出循环的时候意味着i==j，这时候把空位i设置为基数，以此作为下一次递归的拆分位置
        while (i<j){
            //从右往左找，找到比她小的数据那就和他换位置,当i>=j的时候代表着一次内循环完成
            while(i<j&&base<=arr[j])
                j--;
            if(i<j){
                arr[i]=arr[j];
            }
            //从左往右找，只要大于等于基准值，那么就互换位置
            while(i<j&&base>arr[i])
                i++;
            if (i<j){
                arr[j]=arr[i];
            }
        }
        arr[i] = base;
        System.out.println(Arrays.toString(arr));
        quickSort(arr,start,i-1);
        quickSort(arr,i+1,end);



    }
}
