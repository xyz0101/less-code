package com.jenkin.common.utils.demo;

/**
 * @author jenkin
 * @className MissingNumber
 * @description TODO
 * @date 2021/2/19 15:33
 */
public class MissingNumber {
    public static void main(String[] args) {
        System.out.println(new MissingNumber().missingNumber(new int[]{  0,1,3,4,5,6}));
    }
    public int missingNumber(int[] nums) {
        int start = 0;
        int end = nums.length-1;
        int mid = 0;
        while(start<end){
            mid = (start+end)>>1;
//            如果中间值不等,说明缺少的数字在左边，否则在右边
            if(nums[mid]!=mid){
               end = mid;
            }else{
                start = mid+1;
            }
        }
        return nums[start]==start?start+1:start;
    }
}
