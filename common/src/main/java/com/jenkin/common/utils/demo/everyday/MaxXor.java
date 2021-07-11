package com.jenkin.common.utils.demo.everyday;

/**
 * @author ：jenkin
 * @date ：Created at 2021/5/16 16:15
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class MaxXor {

    public static void main(String[] args) {
//        int res = 0;
//        System.out.println(res =8^11);
//        System.out.println(res ^ 11);

        System.out.println(new MaxXor().findMaximumXOR(new int[]{14,70,53,83,49,91,36,80,92,51,66,70}));
    }

    public int findMaximumXOR(int[] nums) {
        if (nums.length<2){
            return 0;
        }
        Tire tire = new Tire();
        tire.add(nums[0]);
        int res = 0;
        for (int i=1;i<nums.length;i++){
            int search = tire.search(nums[i]);
            res = Math.max(res,search^nums[i]);
            tire.add(nums[i]);
        }
        return res;
    }

    static class Tire{
        static class Node{
             Node[] nodes = new Node[2];
        }
        Node root = new Node();

        /**
         * 构建前缀树
         * 把当前数字添加到前缀树里面区
         * @param num
         */
        public void add(int num){
            Node currentNode = root;
            for(int i=31;i>=0;i--){
                //获取当前位的值
                int cur = (num>>i)&1;
                if (currentNode.nodes[cur]==null) {
                    currentNode.nodes[cur] = new Node();
                }
                currentNode = currentNode.nodes[cur];
            }
        }

        /**
         * 搜索出num之前出现过的数字里面能够得到最大异或值的数字
         * @param num
         * @return
         */
        public int search(int num){
            int ans = 0;
            Node currentNode = root;
            for(int i=31;i>=0;i--){
                int cur = (num>>i)&1;
                //期望的数字第i位上面的二进制位
                int except = cur;
                //实际能够取到的数字第i为上面的二进制位
                int acu = except;
                // 假如 x^y =z 那么 x = z^y;
                //由于我们期望已获得结果是1 ，所以 期望的值就是 当前值cur与结果1进行异或
                except = cur^1;
                //实际上我们异或的结果当前可能无法是1
                //所以期望位置如果没有数字的话就不能区期望位置，所以使用 except^1 还原成为cur
                acu =currentNode.nodes[except]==null?except^1:except;
                //这个地方是求出当前根据贪心的到的数字
                ans |=(acu<<i);
                currentNode = currentNode.nodes[acu];
            }
            //返回的这个ans一定是在num之前出现过的
            return ans;
        }
    }



}
