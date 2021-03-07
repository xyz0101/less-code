package com.jenkin.common.utils.demo.recall;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/7 16:14
 * @description：
 * 累加数是一个字符串，组成它的数字可以形成累加序列。
 *
 * 一个有效的累加序列必须至少包含 3 个数。除了最开始的两个数以外，字符串中的其他数都等于它之前两个数相加的和。
 *
 * 给定一个只包含数字 '0'-'9' 的字符串，编写一个算法来判断给定输入是否是累加数。
 *
 * 说明: 累加序列里的数不会以 0 开头，所以不会出现 1, 2, 03 或者 1, 02, 3 的情况。
 *
 * 示例 1:
 *
 * 输入: "112358"
 * 输出: true
 * 解释: 累加序列为: 1, 1, 2, 3, 5, 8 。1 + 1 = 2, 1 + 2 = 3, 2 + 3 = 5, 3 + 5 = 8
 * 示例 2:
 *
 * 输入: "199100199"
 * 输出: true
 * 解释: 累加序列为: 1, 99, 100, 199。1 + 99 = 100, 99 + 100 = 199
 *
 * @modified By：
 * @version: 1.0
 */
public class AdditiveNumber {


    public static void main(String[] args) {
        boolean b = new AdditiveNumber().canAdd("101");
        System.out.println(b);
    }


    public boolean canAdd(String num){

        for (int i = 1; i <=num.length(); i++) {
            for (int j=i+1;j<=num.length();j++){
                if(isAddivite( 0,i,i,j,num )){
                    return true;
                }
            }
        }


        return false;
    }

    private boolean isAddivite(int startA,int endA,int startB,int endB,String num ) {
        String stra = num.substring(startA, endA);
        long a =  Long.parseLong(stra);
        String strb = num.substring(startB, endB);
        if ((strb.startsWith("0")&&strb.length()>1)||(stra.startsWith("0")&&stra.length()>1)) {
            return false;
        }
        long b = Long.parseLong(strb);
        String res = String.valueOf(a+b);
        System.out.println(res);
        int endRes = endB + res.length();
        if (endRes ==num.length()&&num.substring(endB, endRes).equals(res)){
            return true;
        }
        if(endRes<num.length()&&num.substring(endB, endRes).equals(res)){
            return isAddivite(startB,endB,endB, endRes,num);
        }

        return false;

    }


}
