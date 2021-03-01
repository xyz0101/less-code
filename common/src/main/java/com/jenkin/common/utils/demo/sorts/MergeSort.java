package com.jenkin.common.utils.demo.sorts;

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

    /**
     * 归并排序，使用分而治之的思想把数组拆分到最小单位，然后排序，拍完序之后合并数组
     * @param arr
     */
    public void sort(int[] arr) {

        //先写归并



        int start = 0,end = arr.length-1;
        int[] temp = new int[arr.length];

        mergeSort(arr,start,end,temp);


    }

    /**
     * 归并排序，首先使用分而治之把数字拆分成若干个小块，直到不可分割为止
     * 当数组元素只有一个的时候默认数组是有序的，然后执行合并排序算法
     * 在合并排序算法里面，由于左右两侧的数组是有序的，所以每次那两个数组的头部进行比较，如果小的就放在一个新数组里面
     * 这样会生成一个新的有序数组
     * 到最后把剩下的数据全部复制到数组的原有位置就好,到最后数组就是有序的
     * @param arr
     * @param start
     * @param end
     * @param temp
     */
    private void mergeSort(int[] arr ,int start,int end,int[] temp) {

        if(start==end){
            return;
        }

        int mid = (start+end)/2;
        //分治法
        mergeSort(arr,start,mid,temp);
        mergeSort(arr,mid+1,end,temp);
        int i=0;
        int j=0;
//        合并数组
        while(true){
            if((start+i)>mid||(mid+1+j)>end){
                if((start+i)<=mid){
                    temp[i+j]=arr[start+i];
                    i++;

                }else if((mid+1+j)<=end){
                    temp[i+j]=arr[mid+1+j];
                    j++;
                }

            }else{
                if(arr[start+i]<arr[mid+1+j]){
                    temp[i+j]=arr[start+i];
                    i++;
                }else{
                    temp[i+j]=arr[mid+1+j];
                    j++;
                }
            }
            if((i+j)>end-start){
                System.arraycopy(temp,0,arr,start,end-start+1);
                break;
            }



        }













    }


}
