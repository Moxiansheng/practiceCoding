package sort;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class LinkedListQuickSort {
    static ListNode res = null;
    public static class ListNode{
        public int val;
        public ListNode next = null;

        public ListNode(){

        }

        public ListNode(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(4);
        ListNode temp = head;
        for (int i = 1; i < 5; i++) {
            temp = temp.next = new ListNode(i*i);
        }
        temp.next = new ListNode(0);
        quickSort(head, null);
        System.out.println(res.val);
    }

    public static void quickSort(ListNode head, ListNode tail){
        if(head == null || head.next == null || head == tail || head.next == tail){
            res = head;
            return;
        }
        ListNode pivot = head;
        ListNode pre = head;
        ListNode cur = head.next;
        while(cur != tail){
            if(cur.val < pivot.val ){
                if((pre = pre.next) != cur){
                    int temp = cur.val;
                    cur.val = pre.val;
                    pre.val = temp;
                }
            }
            cur = cur.next;
        }
        ListNode newHead = pivot.next;
        res = newHead;
        pivot.next = pre.next;
        pre.next = pivot;
        quickSort(pivot.next, null);
        quickSort(newHead, pivot);

    }
}
