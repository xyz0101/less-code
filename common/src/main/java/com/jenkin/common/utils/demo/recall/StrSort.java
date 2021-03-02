package com.jenkin.common.utils.demo.recall;

import java.util.*;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/2 21:04
 * @description：字符串的排列
 * 输入一个字符串，打印出该字符串中字符的所有排列。
 *
 * 你可以以任意顺序返回这个字符串数组，但里面不能有重复元素。
 *
 * 示例:
 *
 * 输入：s = "abc"
 * 输出：["abc","acb","bac","bca","cab","cba"]
 * @modified By：
 * @version: 1.0
 */
public class StrSort {
    StringBuilder sb = new StringBuilder();
    public static void main(String[] args) {
        String str = "aba";
        Stack<Character> stack = new Stack<>();
        Set<String> res = new HashSet<>();
        new StrSort().sort(str,stack,res,str.length());
        System.out.println(res);
         res.toArray(new String[0]);
    }

    private void sort(String str, Stack<Character> stack, Set<String> res,int len) {

       if(stack.size()==len){
           stack.forEach(item->sb.append(item));
           res.add(sb.toString() );
           sb.delete(0,sb.length());
       }
        for (int i = 0; i < str.length(); i++) {
            char a = str.charAt(i);
             String newStr = new StringBuilder(str).deleteCharAt(i).toString();
            stack.add(a);
            this.sort(newStr,stack,res,len);
            stack.pop();
        }



    }


}
