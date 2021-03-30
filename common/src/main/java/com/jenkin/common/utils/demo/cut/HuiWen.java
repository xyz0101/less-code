package com.jenkin.common.utils.demo.cut;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/22 21:08
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class HuiWen {


    public static void main(String[] args) {
        //abcbadfdaaaacs
        int c = new HuiWen().maxHuiwen("abcbadfdaaaacs");
        System.out.println(c);
        StringBuilder max =new StringBuilder("12");
        max.insert(0,"q");
        System.out.println(max);
    }


    public int maxHuiwen(String s){
        int n = s.length();
        int count = 0;
        for(int i=0;i<n;i++){

            int left=i;
            int right=i;
            while(left>=0&&right<n){
                if(s.charAt(left--)==s.charAt(right++)){
                    count++;
                }else{
                    break;
                }

            }
             left=i;
             right = i+1;
            while(left>=0&&right<n){
                if(s.charAt(left--)==s.charAt(right++)){
                    count++;
                }else{
                    break;
                }
             }



        }
    return count;


    }




}
