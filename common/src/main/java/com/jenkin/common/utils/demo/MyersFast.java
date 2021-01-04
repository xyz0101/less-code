package com.jenkin.common.utils.demo;

/**
 * @author jenkin
 * @className MyersFast
 * @description TODO
 * @date 2020/12/31 14:56
 */
public class MyersFast {

    public static void main(String[] args) {
        myers();
    }

    private static void myers() {
        String str1 = "ABCDEF";
        String str2 = "DAEWFABC";
        int m = str1.length();
        int n =str2.length();
        int maxD = (m+n+1)/2;
        int delta = m-n;
        boolean isOdd = delta%2!=0;
        for (int d = 0; d < maxD; d++) {
            for (int k = -d; k < d; k+=2) {

                if(isOdd&&(k>delta-(d-1)&&k<=delta+(d-1))){

                }

            }
            for (int k = -d; k < d; k+=2) {

                if(!isOdd&&(k>-d-delta&&k<=d-delta)){

                }

            }
        }
    }


}
