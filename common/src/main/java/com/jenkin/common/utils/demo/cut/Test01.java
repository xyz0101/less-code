package com.jenkin.common.utils.demo.cut;

import java.util.Arrays;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/21 14:25
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Test01 {

    //[1,3,5,2,4],[1,2,3,4,5]
    public static void main(String[] args) {
//        new Test01().getMaxValue(new int[]{1,3,5,2,4},new int[]{1,2,3,4,5});
//        new Test01().getMaxValue(new int[]{1,100},new int[]{2,1});
//        new Test01().getMaxValue(new int[]{1,2},new int[]{2,1,3});
        Boolean[][] temp = new Boolean[2][2];
        System.out.println(temp[0][0]);

        System.out.println(new Test01().isHuiwen(0,2,new char[]{'1','2','3','2','1'}));

    }

    public boolean isHuiwen(int i,int j,char[] arr){

        int mid = (i+j)/2;
        int left =  (i+j)%2;

            while(i<j){
                if(arr[i++]!=arr[j--]) return false;
            }


        return true;
    }

        /**
         * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
         *
         *
         * @param nums int整型一维数组
         * @param values int整型一维数组
         * @return int整型
         */
        public int getMaxValue (int[] nums, int[] values) {
            int m = nums.length;
            int n = values.length;

            int[] dp = new int[m+1];
            dp[0]=0;

            int count = 1;

            int i=0;
            int j=m-1;
            while(i<=j){
                int a=nums[i]*values[count-1]+ (count>=n?0: (nums[j]*values[count]));
                int b= (count>=n?0:(nums[i]*values[count]))+nums[j]*values[count-1];
                int val ;
                if(a>b) {
                    val=nums[i]*values[count-1];
                    System.out.println("nums: "+nums[i]+"  value "+values[count-1]);

                    i++;
                }
                else {
                    val=nums[j]*values[count-1];
                    System.out.println("nums: "+nums[j]+"  value  "+values[count-1]);

                    j--;
                }
                dp[count]=dp[count-1]+val;
                count++;
            }
            System.out.println(Arrays.toString(dp));
            return dp[m];
        }

}
