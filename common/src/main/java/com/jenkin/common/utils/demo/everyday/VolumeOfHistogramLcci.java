package com.jenkin.common.utils.demo.everyday;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @author jenkin
 * @className VolumeOfHistogramLcci
 * @description
 * 面试题 17.21. 直方图的水量
 * 给定一个直方图(也称柱状图)，假设有人从上面源源不断地倒水，最后直方图能存多少水量?直方图的宽度为 1。
 *
 *
 *
 * 上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的直方图，在这种情况下，可以接 6 个单位的水（蓝色部分表示水）。 感谢 Marcos 贡献此图。
 *
 * 示例:
 *
 * 输入: [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出: 6
 * @date 2021/4/2 10:12
 */
public class VolumeOfHistogramLcci {

    public static void main(String[] args) {
        int trap = new VolumeOfHistogramLcci().trap(new int[]{3,1,0,3});
        System.out.println(trap);
    }

    public int trap(int[] height) {

        int res = 0;
        Deque<Integer> downStack = new LinkedList<>();
        for(int i=0;i<height.length;i++){
            while(!downStack.isEmpty()&&height[downStack.peek()]<height[i] ){

                Integer currentIndex  = downStack.pollFirst();
                if(downStack.isEmpty()) break;
                Integer leftIndex = downStack.peekFirst();
                int left = height[leftIndex];
                int width = i-leftIndex-1;
                int diff = Math.min(left,height[i])-height[currentIndex];
                res+=(diff*width);
            }
            downStack.push(i);

        }


        return res;
    }

}
