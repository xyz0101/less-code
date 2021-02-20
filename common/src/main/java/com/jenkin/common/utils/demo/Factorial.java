package com.jenkin.common.utils.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jenkin
 * @className Factorial
 * @description  计算一个数组的元素排列组合
 * 例如：[1,2,3] 会得到结果：
 * [1, 2, 3]
 * [1, 3, 2]
 * [2, 1, 3]
 * [2, 3, 1]
 * [3, 1, 2]
 * [3, 2, 1]
 * @date 2020/12/16 13:43
 */
public class Factorial {
    public static void main(String[] args) {
        int[] arr = new int[]{1,2,3,4
        };
        List<Integer> parent = new ArrayList<>();
        getFactorial(parent,arr);

    }

    private static void getFactorial(List<Integer> parent,int[] arr ) {
        if(arr.length==0){
            System.out.println(parent);
        }
        for(int j = 0;j<arr.length;j++){
            int[] newArr = getNewArr(arr, j);
            List<Integer> newParent = new ArrayList<>(parent);
            newParent.add(arr[j]);
            getFactorial(newParent,newArr);
        }
        parent.clear();
    }

    private static int[] getNewArr(int[] arr, int j) {
        int[] newarr = new int[arr.length - 1];
        int k=0;
        for (int i = 0; i < arr.length; i++) {
            if (i!=j) {
                newarr[k] = arr[i];
                k++;
            }
        }
//        System.out.println("当前："+arr[j]+"  新数组："+Arrays.toString(newarr));
        return newarr;
    }
}
