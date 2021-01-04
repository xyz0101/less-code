package com.jenkin.common.utils.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/25 21:26
 * @description：Myers 差分算法
 * @modified By：
 * @version: 1.0
 */
public class Myers {

    public static void main(String[] args) {
        String str1 = "ABCDEF";
        String str2 = "GHIJK";
        myers(str1.toCharArray(),str2.toCharArray());
        /**
         * -A,B,-C,-D,+A,+S,E,+D,+C,F,+D
         */
    }

    private static void myers(char[] arr1, char[] arr2) {

        int m = arr1.length;
        int n =arr2.length;
        //需要移动的步数d 最大的值就是m+n
        int maxD = m+n;
        //k的最大取值范围 [—d ,+d]
        //k的计算公式 ： k=x-y  x=k+y   y=x-k
        //存储K值上面最大的X坐标
        Map<Integer,Integer> xPositionsInK = new HashMap<>();
        List<  Map<Integer,Integer>> dPosition = new ArrayList<>();
//        List<String> temp = new ArrayList<>();
        //需要移动的步数
       outloop: for (int d = 0; d <= maxD; d++) {
           Map<Integer,Integer> temp = new HashMap<>();

            //移动当前步数下K的取值范围,这里面会记录下当前k上面最大的x坐标
            //因为每次k都有两个可选项，所以k的步长是2
          loop: for (int k = -d; k <=d; k+=2) {

                int up = xPositionsInK.get(k+1)==null?0:xPositionsInK.get(k+1);
                int right = xPositionsInK.get(k-1)==null?0:xPositionsInK.get(k-1);


                //判断当前k线上面的元素是从上一个元素怎么过来的
                //有两种情况 ：
                // 一种是当当前的K是 -d的时候，也就是怎样才能通过移动d步到 -d这条k线上面来？
                //那就只有可能是一直向下移动，不然就会导致步数不够，那么
                // 1、向下移动过来的，那么上一个元素的k肯定是 k+1
                //2、 向右移动过来的，那么k=k-1
                //在一种情况就是当K不等于 d的时候 那么就判断上一个 k+1和k-1 的元素哪个大
                //如果k+1大那么就是向下移动过来的，否则就是向右
                //（我们默认遵循先删除后增加的原则，所以比较的是X坐标，X坐标越大代表走得越远）
                boolean isDown = k==-d ||( k!=d&&up>right );
                //如果是向下移动那么前一个的k就是k+1，否则就是k-1
                int preK = isDown?k+1:k-1;
                //根据公式k=x-y 得出 前一个的坐标是
                int preX =xPositionsInK.get(preK)==null?0:xPositionsInK.get(preK);
                int preY = preX-preK;
                //既然得到了前一个的坐标，那么当前坐标就根据移动方向可以得来
                int currentX = isDown?preX:preX+1;
                int currentY = currentX-k;
                int endX = currentX;
                int endY = currentY;
                //这时候就需要判断当前点后面是有相等的节点，循环走斜线，直到遇到不相等的为止
                while(endX<m&&endY<n&&(arr1[endX]==arr2[endY])){
                    endX++;endY++;

                }
                //记录当前的X坐标

                xPositionsInK.put(k,endX);
                temp.put(k,endX);
                 if(endX==m&&endY==n){
                     dPosition.add(temp);
                     break outloop;
                }









            }
           dPosition.add(temp);
        }
        for (int i = 0; i < dPosition.size(); i++) {
            System.out.println(dPosition.get(i));
        }
//        System.out.println(dPosition);
        List<Snake> snakes = generateSnake(dPosition, m, n);
        printSnake(arr1,arr2,snakes);

    }


    private static void printSnake(char[] arr1, char[] arr2, List<Snake> snakes) {
        System.out.println(snakes);
        //y 的偏移量，如果遇到斜线，那么偏移量加一
        int yOffset = 0;
        int index = 0;
        for (Snake snake : snakes) {
            //判断是横着走（删除）还是竖着走（新增）或者是斜着走（不变）
            int preX = snake.getPreX();
            int midX = snake.getMidX();
            int currentX = snake.getCurrentX();
            //如果当前蛇形线的索引为0，也就是走第一步的时候前驱结点不是0
            //那意思是一直到prex都是相等的
            if(index==0&&preX>0){
                for (int x = 0; x <preX; x++) {
                    System.out.println( arr1[x]);
                    yOffset++;
                }
            }
            //判断前驱结点和前驱结点的下一个的差值
            if(midX-preX>0){
             //向右移动代表删除
                System.out.println("-"+arr1[preX]);
            }else{
                System.out.println("+"+arr2[yOffset]);
                yOffset++;
            }
            //然后如果当前的x和midx如果还不相等，就代表着还有相同的值

            for (int i = midX;i<currentX  ;i++){
                System.out.println( arr1[i]);
                yOffset++;
            }
    index++;
        }

    }



    private static  List<Snake>  generateSnake(List<Map<Integer, Integer>> dPosition, int m, int n) {
        int d = dPosition.size();
        int x = m,y=n;
        List<Snake> snakes = new ArrayList<>();
        for (int i = d-1; i >0; i--) {
            Map<Integer, Integer> kmap = dPosition.get(i);
            Map<Integer, Integer> kmapPre = dPosition.get(i-1);

            int k =x-y;
            System.out.println( "k==:"+k+" , "+ kmap);
            int currentX = kmap.get(k)==null?0:kmap.get(k);
            int currentY = currentX-k;

            int up = kmapPre.get(k+1)==null?0:kmapPre.get(k+1);
            int right = kmapPre.get(k-1)==null?0:kmapPre.get(k-1);

            boolean isDown = k==-d ||( k!=d&&up>right );

            int preK = isDown?k+1:k-1;
            int preX =  kmapPre.get(preK) ==null?0:kmapPre.get(preK);
            int preY = preX-preK;
            x = preX;
            y = preY;

            int midX = isDown?preX:preX+1;

            snakes.add(0,new Snake(preX,midX,currentX));
            System.out.println(currentX+" "+currentY+"  前驱："+x+" "+y);

        }

        return snakes;
    }

    @Data
    @AllArgsConstructor
    static class Snake{
        private int preX;

        private int midX;

        private int currentX;

        @Override
        public String toString() {
            return
                    "【" + preX +
                    ", " + midX +
                    ", " + currentX+"】" ;
        }
    }
    @Data

    static class Position{
        private int x;

        private int y;

        private Position next;

        private boolean isEqual;

    }


}
