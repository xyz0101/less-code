package com.jenkin.common.utils.demo;

import java.util.Stack;

/**
 * @author jenkin
 * @className BinarySearch
 * @description 二分查找
 * @date 2021/2/19 14:34
 */
public class BinarySearch {

    public static void main(String[] args) {
        int search = new BinarySearch().search(new int[]{-1, 0, 3, 5, 9, 12}, 9);
        System.out.println(search);
    }

    public int search(int[] nums, int target) {
        int start = 0;
        int end = nums.length-1;
        int mid = 0;
        while(start<=end){
            mid = (start+end)/2;
            if(nums[mid]>target){
                end = mid-1;
            }else if(nums[mid]<target){
                start = mid+1;
            }else{
                return mid;
            }

        }
        return -1;


    }
}
