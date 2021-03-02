package com.jenkin.common.utils.demo.cut;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/2 20:38
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class AddNum {

    public static void main(String[] args) {
        int a = 11,b=99;
        int c =new AddNum().add(a,b);
        System.out.println(c);
    }

    private int add(int a, int b) {
        while(b != 0) { // 当进位为 0 时跳出
            int c = (a & b) << 1;  // c = 进位
            a ^= b; // a = 非进位和
            b = c; // b = 进位
        }
        return a;


    }

}
