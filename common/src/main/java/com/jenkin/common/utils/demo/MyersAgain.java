package com.jenkin.common.utils.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.*;

/**
 * @author ：jenkin
 * @date ：Created at 2021/5/27 20:16
 * @menu
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class MyersAgain {


    public static void main(String[] args) {
        String str1 = "ABCADFEDSFG";
        String str2 = "ASDFCVFEDGDC";
//        String str1 = "ABCABBA";
//        String str2 = "CBABAC";
        myers(str1,str2);


    }

    private static void myers(String str1, String str2) {
        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray();
        int m = arr1.length;
        int n = arr2.length;

        //我们需要得到一张图
        //横坐标标识步数，纵坐标代表k的取值，横纵坐标对应的值代表在步数d下能够到达的点
        //1、定义一个map，存储我们图里面横向的数据，key为k的值，而value为x坐标，其实也就是步数d
        //2、这个kposition里面的每一个k的取值有上一个k-1或者k+1得来
        Map<Integer,Integer> kPositions = new HashMap<>();
        //2、定义一个集合存储每一步的kPositions
        List<Map<Integer,Integer>> dkPositions = new ArrayList<>();
        // 最大的一个可以走的步数
        int maxd = n + m;
        //一步一步的走
        kPositions.put(1,0);

        dloop: for(int d=0;d<=maxd;d++){
            Map<Integer,Integer> kPositionsTemp = new HashMap<>();
            //每走的一步都是会落在k线上面，而k的取值范围是 -d，d
            boolean breakFlag = false;
            for(int k = -d;k<=d;k+=2){
                int x;
                //k==-d 意味着往下走，也就是全部新增，那么x坐标是不变的，也就是上一个的x坐标
                if(k==-d){
                    x = kPositions.get(k+1);
                    //k==d 意味着 往右边走，也就是代表这一步删除了一个字符，那么x坐标是前一个的x+1
                }else if(k==d){
                    x= kPositions.get(k-1)+1;
                    //上面这是两个边界情况，

                    //下面的情况，当前步的前一步取值有两种，k-1和k+1，我们遵循先删除后添加的规则
                }else{
                    //k = x-y , x = k+y , y = x- k
                    //我们在两个可选项里面判断
                    //怎么求的x？
                    //我们需要判断我们当前位置和上一个位置之间的走向
                    //怎么判断？
                    //用k来判断，从k-1到k 意味着向右边走，那么x需要+1，k+1 到k 意味着向下走，那么x不变，那我们到底是要从k-1过来呢还是从k+1过来呢？？
                    //我们需要选取一个走得最远的点，我认为我是从远点过来的，判断远点的条件就是谁的x大，谁就走得远。
                    // 如果kPositions.get(k-1) 大，那么意味着是从k-1走到k，也就是意味着x需要+1，反之意味着从kPositions.get(k+1) 走到k，意味着 x不变
                    x= kPositions.get(k-1)<kPositions.get(k+1)?kPositions.get(k+1):kPositions.get(k-1)+1;
                }
                //现在我们已经走了一步了，可以计算出这一步的y坐标
                int y = x-k;
                //这时候我们还需要判断是否会有斜线，也就是相等的情况，如果相等意味着走斜线，不计入步数，可以继续走，直到不相等
                while( y<n&&x<m && arr1[x]==arr2[y]){
                    x=x+1;
                    y=y+1;
                }
                kPositions.put(k,x);
                kPositionsTemp.put(k,x);
                //如果出现了 x，y大于等于 m，n的情况意味着已经找到了终点，没必要再走了

                if (x>=m&&y>=n){
                    dkPositions.add(kPositionsTemp);
                    breakFlag = true;

                    break dloop;
                }
            }
            dkPositions.add(kPositionsTemp);
            if (breakFlag) break ;
        }
        for (int i = 0; i < dkPositions.size(); i++) {
            System.out.println(dkPositions.get(i));
        }

        for (int i = 0; i < dkPositions.size(); i++) {
            Map<Integer, Integer> map = dkPositions.get(i);
//            for (int k :map.keySet()){
//                System.out.printf("k: "+k+", x: "+map.get(k)+" ,y: "+(map.get(k)-k)+"---|");
            for(int j=11;j>=-11;j--){
                Integer x = map.get(j);
                if(x!=null){
                    System.out.println(x+","+(x-j));
                }else{
                    System.out.println();
                }

            }
//            }
            System.out.println("------------");
        }


        Stack<Snack> snacks = generateSnake(dkPositions, arr1, arr2);
        printSnack(snacks,arr1,arr2);
    }

    private static void printSnack(Stack<Snack> snacks, char[] arr1, char[] arr2) {

        int x = 0;
        int y = 0;
        //从原点开始
        while(x<arr1.length&&y<arr2.length&&arr1[x]==arr2[y]){
            System.out.println(arr2[y]);
            x++;y++;
        }
        while(!snacks.isEmpty()){
            Snack snack = snacks.pop();
            Boolean right = snack.right;
            x = snack.x;
            y = snack.y;
            //right 为空代表是最后一个点了，直接退出
            if (right==null) return;
            if (right){
                System.out.println("-"+arr1[x]);
                x++;
                while(x<arr1.length&&y<arr2.length&&arr1[x]==arr2[y]){
                    System.out.println(arr1[x]);
                    x++;y++;
                }
            }else{
                System.out.println("+"+arr2[y]);
                y++;
                while(x<arr1.length&&y<arr2.length&&arr1[x]==arr2[y]){
                    System.out.println(arr2[y]);
                    x++;y++;
                }
            }


        }
    }

    private static Stack<Snack> generateSnake(List<Map<Integer, Integer>> dkPositions, char[] arr1, char[] arr2) {
        Stack<Snack> snacks = new Stack<>();

        int currentX = arr1.length;
        int currentY = arr2.length;
        snacks.push(new Snack(currentX,currentY,null));
        //从终点出发，往前回溯
        for(int d=dkPositions.size()-1;d>0;d--){
            Map<Integer, Integer> preKPositions = dkPositions.get(d-1);
            int k=currentX-currentY;
            //前一个点只能是k+1或者k-1
            //向下走一步得到当前点的前一个点（从k+1到k k变小）
            Integer prex1 = preKPositions.getOrDefault(k + 1,-1);
            //向右走一步得到当前点的前一个点
            Integer prex2 = preKPositions.getOrDefault(k - 1,-1);
            if (prex1==null&&prex2==null) {
                return snacks;
            }
            //需要判断是从哪一个点过来的
            boolean right = prex2!=null;
            int y1 ;
            int y2 ;
            int prey =right? prex2-k+1: prex1-k-1;
            int prex = right?prex2:prex1;
            if(prex1!=null&&prex2!=null){
                y1 = prex1-k-1;
                y2 = prex2-k+1;
                int diff1 = currentX-prex1;
                int diff2 = currentX-prex2;
                right = diff2<diff1;
                if(diff1==diff2){
                    right = Math.abs(y2-currentY)<Math.abs(y1-currentY);
                }
                prey = right?y2:y1;
                prex = right?prex2:prex1;
            }
            snacks.push(new Snack(prex,prey,right));
            currentX = prex;
            currentY = prey;

        }
        return snacks;
    }

    @Data
    @ToString
    @AllArgsConstructor
    static class Snack{
        private int x;
        private int y;
        private Boolean right;
    }
}
