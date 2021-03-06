package com.jenkin.common.utils.demo.recall;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/3 20:21
 * @description：
 * 1079. 活字印刷
 * 你有一套活字字模 tiles，其中每个字模上都刻有一个字母 tiles[i]。返回你可以印出的非空字母序列的数目。
 *
 * 注意：本题中，每个活字字模只能使用一次。
 * 示例 1：
 *
 * 输入："AAB"
 * 输出：8
 * 解释：可能的序列为 "A", "B", "AA", "AB", "BA", "AAB", "ABA", "BAA"。
 * 示例 2：
 *
 * 输入："AAABBC"
 * 输出：188
 *
 *
 * 提示：
 *
 * 1 <= tiles.length <= 7
 * tiles 由大写英文字母组成
 * * @modified By：
 * @version: 1.0
 */
public class ChinaPrinting {
    static Set<String> values = new HashSet<>();
    public static void main(String[] args) {
        new ChinaPrinting().print("AAABBC");
        System.out.println(values.size());
    }

    private int print(String str){
        int length = str.length();
        boolean[] status = new boolean[length];
        char[] chars = str.toCharArray();
//        for (int i = 0; i < chars.length-1; i++) {
            dfs(0, chars,new StringBuilder(""),status);
//        }

        return 1;

    }

   private void dfs(int deep,char[] chars,StringBuilder sb,boolean[] status){
        if(deep>=chars.length){
            return;
        }
       for (int i = 0; i < chars.length; i++) {
           if(status[i]){
               continue;
           }
           sb.append(chars[i]);
           status[i]=true;
           dfs(deep+1,chars,sb,status);
           status[i]=false;
           values.add(sb.toString());
           sb.deleteCharAt(sb.length()-1);

       }

   }

}
