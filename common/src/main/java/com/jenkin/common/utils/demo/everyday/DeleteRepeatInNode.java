package com.jenkin.common.utils.demo.everyday;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jenkin
 * @className DeleteRepeatInNode
 * @description TODO
 * @date 2021/3/25 14:49
 */
public class DeleteRepeatInNode {

    public static void main(String[] args) {
        DeleteRepeatInNode deleteRepeatInNode = new DeleteRepeatInNode();
//        ListNode node = deleteRepeatInNode.deleteDuplicates(deleteRepeatInNode.getNode(new int[]{1, 2, 3,3}));
        ListNode node = deleteRepeatInNode.deleteDuplicates1(deleteRepeatInNode.getNode(new int[]{1,1,2,3 }));

        ReentrantLock lock = new ReentrantLock();
        lock.lock();

        while (node!=null){
            System.out.println(node.val);
            node=node.next;
        }


    }




    public ListNode deleteDuplicates1(ListNode head) {
        int cnt = 0;
        ListNode node =head;
        ListNode n= null;
        ListNode res= null;
        int repeatNum = -1000;
        while(node!=null){
            ListNode next = node.next;
            if(next!=null&&node.val==next.val){
                node.next = next.next;
                cnt++;
                repeatNum = node.val;
            }else{
                if(cnt>0){
                    if(n!=null){
                        n.next=repeatNum==node.val?null:node;
                    }else{
                        n=repeatNum==node.val?null:node;
                    }
                    res = next==null&&res==null?n:res;
                }else{
                    if(n==null) {
                        n=node;
                        res =n;
                    }
                    else{
                        res=res==null?n:res;
                        n.next=node;
                        n=n.next;
                    }
                }

                repeatNum=-1000;
                cnt=0;
            }
            node = node.next;
        }
        if(cnt>0){
            if(n!=null){
                n.next=null;
            }
        }
        return res;

    }




    public ListNode deleteDuplicates(ListNode head) {
        int cnt = 0;
        ListNode node = head;
        Stack<ListNode> stack = new Stack<>();
        while(node!=null){
            int val = !stack.isEmpty()?stack.peek().val:-1000;
            if(!stack.isEmpty()&&node.val!=stack.peek().val){
                checkStack(stack,val);
            }
            stack.add(node);
            node = node.next;
        }
        if(stack.size()==0) return null;
        if(stack.size()==1) return  new ListNode(stack.pop().val);
        checkStack(stack,stack.peek().val);
        if(stack.size()==0) return null;
        if(stack.size()==1) return new ListNode(stack.pop().val);

        List<ListNode> list = new ArrayList<>();
        while(!stack.isEmpty()){
            list.add(stack.pop());
        }
        head = list.get(list.size()-1);
        head.next=null;
        node = head;
        if(list.size()>1){
            System.out.println(list.get(list.size()-1).val);
            for(int i=list.size()-2;i>=0;i--){
                node.next = list.get(i);
                 System.out.println(list.get(i).val);
                node = node.next;
            }
            node.next=null;
        }


        return head;

    }

    public void checkStack(Stack<ListNode> stack, int val){
        if(stack.isEmpty()) return;
        ListNode temp = stack.pop();
        int c=0;
        while(!stack.isEmpty()&&val== stack.peek().val ){
            stack.pop();
            c++;
        }
        if(c==0) stack.add(temp);
    }

  public static class ListNode {
      int val;
     ListNode next;
     ListNode() {}
     ListNode(int val) { this.val = val; }
     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 }


 public ListNode getNode(int[] arr){
     if (arr.length ==0) {
         return null;
     }
     ListNode node = new ListNode(arr[0]);
     if (arr.length==1) {
         return node;

     }
     ListNode temp = node;
     for (int i = 1; i < arr.length; i++) {
         temp.next = new ListNode(arr[i]);
         temp = temp.next;
     }
     return node;
 }

}
