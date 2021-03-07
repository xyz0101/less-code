package com.jenkin.common.utils.demo.doublesplit;

import java.util.*;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/7 21:00
 * @description：
 * 300. 最长递增子序列
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 *
 * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。
 *
 *
 * 示例 1：
 *
 * 输入：nums = [10,9,2,5,3,7,101,18]
 * 输出：4
 * 解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。
 * 示例 2：
 *
 * 输入：nums = [0,1,0,3,2,3]
 * 输出：4
 * 示例 3：
 *
 * 输入：nums = [7,7,7,7,7,7,7]
 * 输出：1
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 2500
 * -104 <= nums[i] <= 104
 *
 *
 * 进阶：
 *
 * 你可以设计时间复杂度为 O(n2) 的解决方案吗？
 * 你能将算法的时间复杂度降低到 O(n log(n)) 吗?
 * @modified By：
 * @version: 1.0
 */
public class LongAscSubStr {

    public static void main(String[] args) {
        new LongAscSubStr().lengthOfLIS(new int[]{10,9,2,5,3,7,101,18});
    }
    public int lengthOfLIS(int[] nums) {

        int start = 0;
        int end = nums.length-1;


//        getMin(start,end,nums);
        System.out.println(useDp(nums));


        return 0;
    }

    public  int useDp(int[] nums){

        int[] dp = new int[nums.length];


        for (int i = 0; i <nums.length; i++) {
            for (int j = 0; j <i; j++) {
                if(nums[j]<nums[i]){
                    dp[i]=Math.max(dp[j]+1,dp[i]);
                }
            }

        }
        System.out.println(Arrays.toString(dp));
        return dp[nums.length-1];

    }


    Queue<Integer> res = new LinkedList<>();
    public int getMin(int start ,int end,int[] nums){
        while(start<end){
            int mid = (start+end)/2;
            int a =getMin(start,mid-1,nums);
            int b =getMin(mid+1,end,nums);
            System.out.println(a+"  "+b);
            if (a!=-1&&b!=-1) {
                if(a>=b){
//                    if (res.peek()!=null) {
//                        res.poll();
//                    }
                    res.add(b);
                }else{
                    res.add(a);
                }
            }

            return nums[mid];
        }
        return -1;
    }










}
