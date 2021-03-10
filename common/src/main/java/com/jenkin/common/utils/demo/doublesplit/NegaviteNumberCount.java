package com.jenkin.common.utils.demo.doublesplit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/8 16:39
 * @description：
 * 1351. 统计有序矩阵中的负数
 * 给你一个 m * n 的矩阵 grid，矩阵中的元素无论是按行还是按列，都以非递增顺序排列。
 *
 * 请你统计并返回 grid 中 负数 的数目。
 *
 *
 *
 * 示例 1：
 *
 * 输入：grid = [[4,3,2,-1],[3,2,1,-1],[1,1,-1,-2],[-1,-1,-2,-3]]
 * 输出：8
 * 解释：矩阵中共有 8 个负数。
 * 示例 2：
 *
 * 输入：grid = [[3,2],[1,0]]
 * 输出：0
 * 示例 3：
 *
 * 输入：grid = [[1,-1],[-1,-1]]
 * 输出：3
 * 示例 4：
 *
 * 输入：grid = [[-1]]
 * 输出：1
 *
 *
 * 提示：
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 100
 * -100 <= grid[i][j] <= 100
 *
 *
 * 进阶：你可以设计一个时间复杂度为 O(n + m) 的解决方案吗？
 * @modified By：
 * @version: 1.0
 */
public class NegaviteNumberCount {

    public static void main(String[] args) {
        NegaviteNumberCount negaviteNumberCount = new NegaviteNumberCount();
        int count = negaviteNumberCount.getCount(new int[][]{
                {4,3,2,-1},
                {3,2,1,-1},
                {1,1,-1,-2},
                {-1,-1,-2,-3}
        });

        System.out.println(count);
    }

    private int getCount(int[][] nums) {
        int count =0;
        for (int i = 0; i < nums.length; i++) {
            count+=getSubCount(nums[i],0,nums[i].length-1);
        }
        return count;
    }

    private int getSubCount(int[] num,int start,int end) {

        while (start<=end){
            int mid = (start+end)/2;
            if (num[mid]<0){
                return getSubCount(num,start,mid-1)+end-mid+1;
            }else{
                return getSubCount(num,mid+1,end);
            }
        }
        return 0;
    }


}
