package com.jenkin.common.utils.demo;

import java.util.Arrays;

/**
 * @author jenkin
 * @className QuickSort
 * @description TODO
 * @date 2021/2/25 15:10
 */
public class QuickSort {


    public static void main(String[] args) {
        int[] arr = new int[]{4,2,1,4,6,3,2,4,5,2,6,5,5,0,9,7,88,7,9};
         new QuickSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }


    private void sort(int[] arr) {
        quickSort(arr,0,arr.length-1);

    }

    /**
     * 快速排序的原理 挖空填补和分而治之
     * 需要有一个基准位置，这个基准位置一般默认是第一个，以基准位置的这个数字作为每一次排序比较的关键点
     * 快速排序每次会把基准位置左边的数变得比他小，右边的数变得比他大，
     * 首先我们会把基准位置挖空，然后从右往左找比他小的数据填空（填到基准位），这时候空的位置就会变为刚刚找到的数字那里，
     * 然后我们再从左往右找一遍比他大的数字，重复上面操作，填空，一直找到i==j（也就是左右碰撞了）就代表完成一轮，这时候i或者j必然是空的，
     * 把基准数字填上去就好了， 当经过上面一轮排序之后，基准数据的位置可能会发生变化
     * 我们会使用这个变化后的基准位置去进行一个排序，直到基准位置不在变化
     */
    private void quickSort(int[] arr,int start,int end){
        //这是递归的终止条件，也就是待排序区间只有一个数了，不用再排序了
        if(start>end){
            return ;
        }
        int i=start;
        int j=end;
        //每次获取第一个为基准数
        int base = arr[i];
        while(i<j){
            //从右往左找到比他小的数据填空
            while(i<j&&arr[j]>=base) {
                j--;
            }
            if(i<j){
                arr[i]=arr[j];
            }
            //从左往右找到比他大的数据填空
            while(i<j&&arr[i]<base){
                i++;
            }
            if(i<j){
                arr[j]=arr[i];
            }
        }
        //一轮排序完成，把基数赋值给当前位置
        arr[i] = base;
//        以当前位置为基准，我们已经知道当前位置的左边都是比他小的，
//        右边都是比他大的，因此可以对左右两边分别再使用快速排序
        quickSort(arr,start,i-1);
        quickSort(arr,i+1,end);
    }


}
