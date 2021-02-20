package com.jenkin.common.utils.demo;

import java.util.List;

/**
 * @author jenkin
 * @className MergeTwoLists
 * @description TODO
 * @date 2021/2/19 17:03
 */
public class MergeTwoLists {
    public static void main(String[] args) {
        ListNode listNode = new MergeTwoLists().mergeTwoLists(new ListNode(new int[]{ 1,2}), null );
        System.out.println(listNode);
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1==null) return l2; else if(l2==null) return l1;
        ListNode start = l1.val<l2.val?l1:l2;
        ListNode temp = start==l1?l2:l1;
        if (start==l1) {
            l2=null;
        }else{
            l1=null;
        }
        ListNode before = null;
        while(temp != null){
            if(temp.val>=start.val){
                ListNode sn = start.next;
                if (sn == null) {
                    start.next = temp;
                    break;
                }
                if(sn.val > temp.val) {
                    start.next = temp;
                    temp = temp.next;
                    start.next.next = sn;
                    before = start.next;
                    start = sn;
                }else{
                    start = start.next;
                }
            } else{
                if(before!=null) {
                    before.next = temp;
                    temp = start;
                    start=before.next;
                    before=null;
                }

            }


        }
        return l2==null?l1:l2;
    }
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
        ListNode(int[] arr){

            this.val=arr[0];
            ListNode before = this;
            if (arr.length>1) {
                for (int i = 1; i < arr.length; i++) {
                    ListNode listNode = new ListNode(arr[i]);
                    before.next = listNode;
                    before = listNode;
                }
            }

        }

        @Override
        public String toString() {
            ListNode node = this;
            StringBuilder sb = new StringBuilder( );
            sb.append(node.val);
             while (node.next!=null){
                 sb.append(",").append(node.next.val);
                 node = node.next;
             }
             return sb.toString();
        }
    }
}
