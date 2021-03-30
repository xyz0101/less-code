package com.jenkin.common.utils.demo.cut;

import java.util.*;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/20 22:39
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Test {
    //[[7,1000000000,1],[15,3,0],[5,999999995,0],[5,1,1]]
    //[[23,8,0],[28,29,1],[11,30,1],[30,25,0],[26,9,0],[3,21,0],[28,19,1],[19,30,0],[20,9,1],[17,6,0]]
    public static void main(String[] args) {
        int ck077 = new Test().getNumberOfBacklogOrders(new int[][]{
                {23,8,0},{28,29,1},{11,30,1},{30,25,0},{26,9,0},{3,21,0},{28,19,1},{19,30,0},{20,9,1},{17,6,0}
        });
        System.out.println(ck077);
    }
    public int getNumberOfBacklogOrders(int[][] orders) {


        //每当遇到销售订单的时候需要检查前面积压的价格最高采购订单有没有大于等于当前金额
        //每当遇到采购订单的时候需要检查前面积压的价格最低的销售订单有没有小于等于当前金额

        //定义两个队列，分别存放价格最低的销售订单和价格最高的采购

        PriorityQueue<int[]> sell = new PriorityQueue<>(Comparator.comparingInt(o -> o[0]));
        PriorityQueue<int[]> buy = new PriorityQueue<>( (o1,o2)->o2[0]-o1[0]);

        //分别计算
        int n = orders.length;
        for(int i=0;i<n;i++){
            int[] order = orders[i];
            int currentCost = order[0];
            int currentNum = order[1];

            //如果是采购订单，检查销售
            if(order[2]==0){

                checkSell(sell,order);
                buy.add(order);
            }else{
                //如果是销售订单，检查采购

                sell.add(order);
                checkBuy(buy,order);

            }
        }
        System.out.println( "sell");
        for (int[] ints : sell) {
            System.out.println(Arrays.toString(ints));
        }
        System.out.println( "buy");
        for (int[] ints : buy) {
            System.out.println(Arrays.toString(ints));
        }



        long orderNum = 0;
        for (int[] s : sell) {
            orderNum+=s[1];
        }
        for (int[] s : buy) {
            orderNum+=s[1];
        }
        return ((int)(orderNum%1000000007));












    }

    private void checkBuy(PriorityQueue<int[]> buy,int[] order) {
        int currentCost = order[0];
        int currentNum = order[1];
        currentNum=order[1];
        if(buy.size()==0||currentNum<=0){
            return;
        }
        int[] maxBuy = buy.peek();
        int cost = maxBuy[0];
        int num = maxBuy[1];
        if(currentCost<=cost){
            buy.poll();
            if(num>=currentNum){
                num = num-currentNum;
                maxBuy[1]=num;
                buy.add(maxBuy);
                order[1]=0;
                return;
            }else if(num<currentNum){
                num =  currentNum-num;
                order[1]=num;
                if(num==0) return;
                else
                  checkBuy(buy,order);
            }
        }
    }

    private void checkSell(PriorityQueue<int[]> sell, int[] order) {
        int currentCost = order[0];
        int currentNum = order[1];

        currentNum=order[1];
        if(sell.size()==0||currentNum<=0){
           return;
        }
        int[] minSell = sell.peek();
        int cost = minSell[0];
        int num = minSell[1];
        if(currentCost>=cost){
            sell.poll();
            if(num>=currentNum){
                num = num-currentNum;
                minSell[1]=num;
                sell.add(minSell);
                order[1]=0;
                return;
            }else if(num<currentNum){
                num = currentNum-num;
                order[1]=num;
                if(num==0) return;
                else
                checkSell(sell,order);

            }
        }
    }
}
