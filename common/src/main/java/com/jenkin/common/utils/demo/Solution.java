package com.jenkin.common.utils.demo;

import java.util.*;

class Solution {
    public static void main(String[] args) {
        int i = new Solution().openLock(new String[]{"8887", "8889", "8878", "8898", "8788", "8988", "7888", "9888"}, "8888");
        System.out.println(i);
    }
    Set<String> set = new HashSet<>();

    int count = 100000;
    public int openLock(String[] deadends, String target) {
        for(String s :deadends) set.add(s);

        Queue<String> queue = new LinkedList<>();
        Queue<String> newQueue = new LinkedList<>();
        if(set.contains("0000")) return -1;
        queue.add("0000");
        while(!queue.isEmpty()){
            String str = queue.poll();
            if(str.equals(target)){
                return count;
            }
            addSub(newQueue,str);
            if(queue.isEmpty()){
                count++;
                queue = newQueue;
                newQueue = new LinkedList<>();
            }
        }
        return -1;
    }
    public void addSub( Queue<String> queue,String str){
        StringBuilder sb = new StringBuilder(str);
        for(int i=0;i<4;i++){
            char c = sb.charAt(i);
            char addc = c;
            char delc = c;
            if(c=='0'||c=='9'){
                addc = c=='0'?'1':'0';
                delc = c=='0'?'9':'8';
            }else{
                addc = (char)(c+1);
                delc = (char)(c-1);
            }
            sb.setCharAt(i,addc);
            if(!set.contains(sb.toString())){
                queue.add(sb.toString());
            }else{
                sb.setCharAt(i,c);
            }
            sb.setCharAt(i,delc);
            if(!set.contains(sb.toString())){
                queue.add(sb.toString());
            }else{
                sb.setCharAt(i,c);
            }
            sb.setCharAt(i,c);
        }
        set.addAll(queue);
    }
}