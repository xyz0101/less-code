package com.jenkin.common.utils.demo;

import cn.hutool.core.date.DateUtil;
import com.jenkin.common.utils.DateUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/28 11:15
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Test {
    public static void main(String[] args) {
//        double sqrt = Math.sqrt(12);
//        System.out.println((int)sqrt);
//        StringBuilder stringBuilder = new StringBuilder();
//
//        char c = args[0].charAt(1);
//        int[] arr = new int[2];
//        boolean b = c=='.';
//        ArrayList<String> objects = new ArrayList<>();
//        String res = String.join(".", objects);
//
//        Map<String,Integer> count = new HashMap<>();
//        Collections.sort(new ArrayList<>(count.values()));
//        PriorityQueue<String> queue = new PriorityQueue<>(1,(o1,o2)->
//                count.get(o2).equals(count.get(o1)) ?o1.compareTo(o2):
//                count.get(o2)-count.get(o1));
        ArrayList<Object> objects = new ArrayList<>();
        StringBuilder sb = new StringBuilder("0000");

        System.out.println(Math.pow(3,0));
        System.out.println(Integer.toString(3541,37));
    }
    public String evaluate(String s, List<List<String>> knowledge) {
         return s;
    }

    class Item{
        int startIndex;
        int endIndex;
        int count;
        char val;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return val == item.val;
        }

        @Override
        public int hashCode() {
            return Objects.hash(val);
        }
    }
}
