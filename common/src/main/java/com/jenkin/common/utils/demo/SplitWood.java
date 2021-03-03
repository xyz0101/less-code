package com.jenkin.common.utils.demo;

import java.util.Arrays;

/**
 * @author jenkin
 * @className SplitWood
 * @description
 * 题目描述
 * 给定长度为n的数组，每个元素代表一个木头的长度，
 * 木头可以任意截断，从这堆木头中截出至少k个相同
 * 长度为m的木块。已知k，求max（m）。
 * 输入两行，第一行n，k，第二行为数组序列。输出最
 * 大值。
 * 输入
 * 5 5
 * 4 7 2 10 5
 * 输出
 * 4
 * 解释： 最多可以把它分成5段长度为4的木头
 * @date 2021/3/2 9:41
 */
public class SplitWood {
    public static void main(String[] args) {
        int[] woods = new int[]{2,3,2,3,6};
        int num = 4;
        int length =5;
        Arrays.sort(woods);
        int len = new SplitWood().split(woods,num,length);
        System.out.println(len);
    }

    /**
     *
     * @param woods
     * @param num 需要分割的次数
     * @param length 数组长度
     * @return
     */
    private int split(int[] woods, int num, int length) {
        int i=length-1;
        while (true){
            if (i>0) {
                int j=i-1;
                boolean b1 = doSplit(woods, woods[i], num);
                boolean b2 = doSplit(woods, woods[j], num);
                if (b1!=b2) {
                    for (int k=woods[i];k>=woods[j];k--){
                         if(doSplit(woods, k, num)){
                             return k;
                         }
                    }
                }else if(b1&&b2){
                    return woods[j];
                }
            }else{
                break;
            }
            i--;
        }


        return 0;
    }

    private boolean doSplit(int[] woods, int len, int num) {
        for (int i = woods.length - 1; i >= 0; i--) {
            if (woods[i]>=len) {
                num=num-woods[i]/len;
            }else{
                break;
            }
        }
        return num<=0;
    }

}
