package com.jenkin.common.utils.demo.dull_stack;

import java.util.PriorityQueue;
import java.util.Stack;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/24 22:40
 * @description：单调递增栈
 * @modified By：
 * @version: 1.0
 */
public class IncreaseStack {


    public static void main(String[] args) {
        new IncreaseStack().increaseStack(new int[]{1,3,4,2,4,7,9,4,0,1,4,6,8,4,5,3,6,7,2,2,1,4,6});
    }

    private void increaseStack(int[] arr) {

        Stack<Integer> stack = new Stack<>();

        for (int i=0;i<arr.length;i++){

            while(!stack.isEmpty()&&stack.peek()<arr[i]){

                Integer pop = stack.pop();
                System.out.print(pop+",");
            }

            String s = "";

            System.out.println();
            stack.push(arr[i]);


        }







    }


}
