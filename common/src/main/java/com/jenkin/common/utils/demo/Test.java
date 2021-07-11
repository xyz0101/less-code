package com.jenkin.common.utils.demo;

import java.util.*;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/28 11:15
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Test {
    public static void main(String[] args) {
      Map<String,String> map = new TreeMap<>(Comparator.comparingInt(Integer::parseInt));
      map.put("3","123");
      map.put("10","0");
      map.put("5","1");
        System.out.println(map);

    }

    

}