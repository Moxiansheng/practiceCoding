package jobCode;

import java.util.*;
public class ByteDance{
    public static void main(String[] args) {
        System.out.println("Please, a ByteDance offer!");
    }

//    static int n;
//    static int[] nums;
//    static int[] res;
//
//    public static void main(String[] args){
//        Scanner sc = new Scanner(System.in);
//        n = sc.nextInt();
//        int j = sc.nextInt() - 1;
//        nums = new int[n];
//        for(int i = 0; i < n; i++){
//            nums[i] = sc.nextInt();
//        }
//        int min = nums[j], k = j;
//        for(int i = pre(j); i != j; i = pre(i)){
//            if(nums[i] < min){
//                min = nums[i];
//                k = i;
//            }
//        }
//        min++;
//        while(min > 0){
//            while(j != k){
//                nums[j]--;
//                nums[k]++;
//                j = pre(j);
//            }
//            j = pre(j);
//            min--;
//        }
//        for(int n : nums){
//            System.out.print(n+" ");
//        }
//    }
//
//    public static int pre(int j){
//        return --j >= 0 ? j : n - 1;
//    }
}
