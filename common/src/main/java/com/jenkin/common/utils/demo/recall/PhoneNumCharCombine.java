package com.jenkin.common.utils.demo.recall;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/3 21:36
 * @description：给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。
 *
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 * 示例 1：
 *
 * 输入：digits = "23"
 * 输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
 * 示例 2：
 *
 * 输入：digits = ""
 * 输出：[]
 * 示例 3：
 *
 * 输入：digits = "2"
 * 输出：["a","b","c"]
 * 提示：
 *
 * 0 <= digits.length <= 4
 * digits[i] 是范围 ['2', '9'] 的一个数字。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @modified By：
 * @version: 1.0
 */
public class PhoneNumCharCombine {
    static List<String> res = new ArrayList<>();
    String[] charStr = new String[]{"abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
    public static void main(String[] args) {
        String str = "22";
        boolean[] status = new boolean[str.length()];
        new PhoneNumCharCombine().findCombine(0,str,status,new StringBuilder());
        System.out.println(res);

    }

    private void findCombine(int deep,String str,boolean[] status,StringBuilder sb) {

        if(str.equals("")||deep>=str.length()){
            res.add(sb.toString());
            return;
        }
        for (int i = deep; i < str.toCharArray().length; i++) {
            if(status[i]){
                continue;
            }
            int index =  ((int)str.charAt(i)-(int)'0') -2;
            char[] chars = charStr[index].toCharArray();
            status[i]=true;
            for (char c : chars) {
                sb.append(c);
                findCombine(deep+1,str,status,sb);
                sb.deleteCharAt(sb.length()-1);
            }
            status[i]=false;

        }


    }


}
