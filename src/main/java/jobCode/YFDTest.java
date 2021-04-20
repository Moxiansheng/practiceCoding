package jobCode;

import java.util.Scanner;

public class YFDTest {

    static int mod = 1000000007;
    static int target;
    static int n;
    static int[] nums;

        static int[][] mem;
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            n = sc.nextInt();
            nums = new int[n];
            mem = new int[n][n];
            for(int i = 0; i < n; i++){
                nums[i] = sc.nextInt();
            }
            target = sc.nextInt();

            int res = nums[0] <= target ? 1 : 0;
            mem[0][0] = nums[0];

            for(int i = 1; i < n; i++){
                mem[0][i] = mem[0][i-1] | nums[i];
                if(mem[0][i] <= target){
                    ++res;
                }
            }
            for(int i = 1; i < n; i++){
                mem[i][i] = nums[i];
                if(nums[i] <= target){
                    ++res;
                }
                for(int j = i + 1; j < n; j++){
                    mem[i][j] = mem[i - 1][j] & (~nums[j]);
                    if(mem[i][j] <= target){
                        ++res;
                    }
                }
            }

            System.out.println(res);
        }
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int n = sc.nextInt();
//        int[] nums;
//        for(int i = 0; i < n; i++){
//            int k = sc.nextInt();
//            nums = new int[k];
//            for(int j = 0; j < k; j++){
//                nums[j] = sc.nextInt();
//            }
//            if(nums[0] > nums[k-1]){System.out.println(panduandijian(nums));}
//            else{ System.out.println(panduan(nums));}
//        }
//    }
//
//    public static String panduandijian(int[] nums){
//        for(int i = 0; i < nums.length-1; i++){
//            if(nums[i] < nums[i+1]){
//                return "N";
//            }
//        }
//        return "Y";
//    }
//
//    public static String panduan(int[] nums){
//        boolean one = false;
//        int shanfeng = 0;
//        int shangu = 0;
//        for(int i = 0; i < nums.length-1; i++) {
//            if (!one) {
//                if (nums[i] < nums[i + 1]) {
//                    shangu++;
//                    one = true;
//                }
//            } else {
//                if (nums[i] > nums[i + 1]) {
//                    shanfeng++;
//                    one = false;
//                }
//            }
//        }
//        return shangu<=1 && shanfeng == 1 ? "Y" :"N";}

}
