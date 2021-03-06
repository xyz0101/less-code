package com.jenkin.common.utils.demo.recall;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/4 20:49
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class GenerateKuoHao {
    List<String> kuohao = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    private int leftCount = 0;
    private int rightCount = 0;
    public static void main(String[] args) {
        GenerateKuoHao generateKuoHao = new GenerateKuoHao();
        generateKuoHao.generateParenthesis(3);
        System.out.println(generateKuoHao.kuohao);
        System.out.println(generateKuoHao.sb);
    }

    public List<String> generateParenthesis(int n) {
        boolean[] status  = new boolean[n];
        dfs(0,n,"");

        return kuohao;
    }


    public void dfs(int deep,int n ,String start){
        if(leftCount>n||rightCount>leftCount) return;
        if(deep==2*n){
            kuohao.add(start);
            return;
        }
        leftCount++;
        dfs(deep + 1,  n, start+"(");
        leftCount--;
        rightCount++;
        dfs(deep + 1,  n, start+")");
        rightCount--;

    }


}
