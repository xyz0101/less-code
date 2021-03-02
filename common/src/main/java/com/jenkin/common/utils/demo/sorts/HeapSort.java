package com.jenkin.common.utils.demo.sorts;

import java.util.Arrays;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/1 21:47
 * @description：堆排序
 * @modified By：
 * @version: 1.0
 */
public class HeapSort {
    public static void main(String[] args) {
        int[] arr = new int[]{4,2,1,4,6,3,2,4,5,2,6,5,5,0,9,7,88,7,9};


        new HeapSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 堆排序
     * 1、构造堆
     * 2、把首尾元素进行一个交换
     * 3、数组大小-1再次递归上面操作
     * @param arr
     */
    public void sort(int[] arr) {

        buildHeap(arr,arr.length);
        heapSort(arr);

    }

    /**
     * 构造堆的方法
     * 1、找到最底层的第一个父节点
     * 2、最后一个节点的父节点开始网上循环构造堆
     * 3、根据完全层序遍历的性质，父节点之间是连在一起的，
     * 因此只要找到最后一个节点的父节点，就能够依次找到上面的父节点
     * @param arr
     * @param length
     */
    private void buildHeap(int[] arr, int length) {

        int lastParent = (length-1)/2;
        for (int parent = lastParent; parent >=0; parent--) {
            //对这个节点进行堆的构造
            heapfy(arr,parent,length);
        }


    }

    /**
     * 构造大顶堆
     * 判断子节点谁大，就和大的子节点交换位置，然后在对大的子节点构造堆
     * 如果没有子节点那就说明不用递归了
     *
     * 在完全二叉树的层序遍历里面有几个特点
     * 设 当前节点的索引为 c1, ，邻居节点为c2 父节点为p
     * 假设我们在数组的任意位置 i
     * 1、当前节点的父节点的索引等于 ： p= (i-1)/2
     * 2、左子节点的索引等于： c1=2*i+1
     * 3、有子节点的索引等于： c1=2*i+2
     *
     *
     * @param arr 堆数组
     * @param i 需要对哪个节点构造堆
     * @param length 数组的实际逻辑长度
     */
    private void heapfy(int[] arr, int i, int length) {
        if (i>=length){
            return;
        }


        int max = i;
        int c1 = 2*i+1;
        int c2 = 2*i+2;
        if(c1<length&& arr[c1]>arr[max]){
            max =c1;
        }
        if(c2<length&& arr[c2]>arr[max]){
            max = c2;
        }
        if(max!=i) {
            swapArr(arr, max, i);
            //对最大子节点构造堆
            heapfy(arr,max,length);
        }
    }

    /**
     * 父节点和最大节点交换位置
     * @param arr
     * @param max
     * @param parent
     */
    private void swapArr(int[] arr, int max, int parent) {
        int temp =arr[parent];
        arr[parent]=arr[max];
        arr[max]=temp;
    }

    /**
     * 对构造好的堆进行排序
     * @param arr
     */
    private void heapSort(int[] arr) {
        for (int length = arr.length-1; length >= 0; length--) {
            swapArr(arr,length,0);
            heapfy(arr,0,length);
        }



    }
}
