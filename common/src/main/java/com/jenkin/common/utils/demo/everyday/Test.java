package com.jenkin.common.utils.demo.everyday;

import java.util.*;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/18 11:34
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Test {

    public static void main(String[] args) {
        int[] order = new Test().getOrder(new int[][]{
                {1, 2}, {2, 4}, {3, 2}, {4, 1}
        });
        System.out.println(Arrays.toString(order));
    }


    public int[] getOrder(int[][] tasks) {





        Map<Integer, List<ProcessInfo>> treeMap = new TreeMap<>(Comparator.comparingInt(o -> o));
        for(int i=0;i<tasks.length;i++){
            List<ProcessInfo> process =  treeMap.get(tasks[i][0]);
            if(process==null){
                process = new ArrayList<>();
            }
            process.add(new ProcessInfo(i,tasks[i][1]));
            treeMap.put(tasks[i][0],process);
        }

        int[] order = new int[tasks.length];

        List<Integer> keys = new ArrayList<>(treeMap.keySet());
        int time = keys.get(0);
        int index = 0;

        PriorityQueue<ProcessInfo> queue =
                new PriorityQueue<>(Comparator.comparingInt((ProcessInfo o) -> o.time).thenComparingInt(o -> o.index));
        List<ProcessInfo> process = treeMap.get(time);
        treeMap.remove(time);
        if(process!=null&&!process.isEmpty()){
            queue.addAll(process);
        }

        while(queue.peek()!=null){
            ProcessInfo info =  queue.poll();
            order[index++]=info.index;

            int max = time+info.time;
            Iterator<Integer> iterator = keys.iterator();
            while (iterator.hasNext()){
                Integer ik = iterator.next();
                if(ik >max){
                    break;
                }
                if(ik<=time) continue;
                process = treeMap.get(ik);
                if(process!=null&&!process.isEmpty()){
                    queue.addAll(process);
                }
                treeMap.remove(ik);
                iterator.remove();
            }

            time=max;
        }





        return order;




    }

    class ProcessInfo{
        int index;
        int time;
        public ProcessInfo(int index ,int time){
            this.index = index ;
            this.time = time;
        }
    }
}
