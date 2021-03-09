package com.jenkin.common.utils.demo.doublesplit;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/8 17:21
 * @description：852. 山脉数组的峰顶索引
 * 符合下列属性的数组 arr 称为 山脉数组 ：
 * arr.length >= 3
 * 存在 i（0 < i < arr.length - 1）使得：
 * arr[0] < arr[1] < ... arr[i-1] < arr[i]
 * arr[i] > arr[i+1] > ... > arr[arr.length - 1]
 * 给你由整数组成的山脉数组 arr ，返回任何满足 arr[0] < arr[1] < ... arr[i - 1] < arr[i] > arr[i + 1] > ... > arr[arr.length - 1] 的下标 i 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：arr = [0,1,0]
 * 输出：1
 * 示例 2：
 *
 * 输入：arr = [0,2,1,0]
 * 输出：1
 * 示例 3：
 *
 * 输入：arr = [0,10,5,2]
 * 输出：1
 * 示例 4：
 *
 * 输入：arr = [3,4,5,1]
 * 输出：2
 * 示例 5：
 *
 * 输入：arr = [24,69,100,99,79,78,67,36,26,19]
 * 输出：2
 * @modified By：
 * @version: 1.0
 */
public class MountainArr {

    public static void main(String[] args) {
        int[] arr = {3,5,3,2,0};
        int arrIndex = getArrIndex(arr, 0, arr.length - 1);
        System.out.println(arrIndex);
    }

    /**
     * 山脉数组的特点就是这个数两边都比他小
     * @param arr
     * @return
     */
    private static int getArrIndex(int[] arr,int start,int end) {
        while(start<=end){
            int mid =(start+end)/2;
            if(mid - 1>=0&&mid+1<arr.length&& arr[mid]>arr[mid-1]&&arr[mid]>arr[mid+1]){
                return mid;
            }else if(mid+1<arr.length&& arr[mid]<arr[mid+1]&&(mid-1<0|| arr[mid]>arr[mid-1])){
               return  getArrIndex(arr,mid+1,end);
            }else if(mid - 1>=0&& arr[mid]<arr[mid-1]&&(mid+1>arr.length||arr[mid]>arr[mid+1])){
              return  getArrIndex(arr, start, mid-1);
            }
        }
        return -1;
    }


}

