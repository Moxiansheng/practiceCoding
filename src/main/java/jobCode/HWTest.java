package jobCode;

import java.util.*;

public class HWTest {
    static class Node{
        int val;
        Node next;
        public Node(int v){
            val = v;
        }
    }
    public static void main(String[] args) {
        // 删除倒数第n个节点，单向链表
        Node root = new Node(1);
        Node root1 = new Node(2);
        Node root2 = new Node(3);
        Node root3 = new Node(4);
        Node root4 = new Node(5);
        root.next = root1;
        root1.next = root2;
        root2.next = root3;
        root3.next = root4;

        Node res = delete(root, 1);
        while(res != null){
            System.out.println(res.val);
            res = res.next;
        }
    }
    // 1 2 3 4 5
    // 0 1 2 3 4 5
    //

    public static Node delete(Node root, int k){
        Node res = new Node(0);
        res.next = root;

        Node fast = res;
        Node slow = res;
        while(k > 0){
            fast = fast.next;
            k--;
        }

        while(fast.next!=null){
            fast = fast.next;
            slow = slow.next;
        }

        slow.next = slow.next.next;
        return res.next;
    }

//    static int[] count = new int[1];
//    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        chuli(in.nextLine());
//        int res = 0;
//        if(count == null){
//            System.out.print(res);
//            return;
//        }
//        for(int i = 0; i < count.length;){
//            int temp = count[i];
//            int t = i + 1;
//            while(t < count.length && t - i < temp && count[i] == count[t]){
//                count[t++] = 0;
//            }
//            i = t;
//        }
//        for(int n : count){
//            res+=n;
//        }
//        System.out.print(res);
//    }
//
//    public static void chuli(String str){
//        if(str.length() <= 2){
//            count = null;
//        }else{
//            String[] temp = str.substring(1, str.length() - 1).split(", ");
//            if(temp.length < 1){
//                count = null;
//            }else{
//                count = new int[temp.length];
//                for (int i = 0; i < temp.length; i++) {
//                    count[i] = Integer.valueOf(temp[i]) + 1;
//                }
//                Arrays.sort(count);
//            }
//        }
//    }
}
