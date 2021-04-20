package jobCode;

import java.util.*;

public class MeituanTest extends LinkedHashMap<Integer, Integer> implements Map<Integer, Integer> {


    public static void main(String[] args) {
        HashMap<Integer, Integer> hm = new HashMap<>();
        Iterator it = hm.entrySet().iterator();

        for(Entry<Integer, Integer> en : hm.entrySet()){
            en.getKey();
            en.getValue();
        }
//        Scanner sc = new Scanner(System.in);
//        int row = sc.nextInt();
//        int col = sc.nextInt();
//        int[][] nums = new int[row][col];
//        for(int i = 0; i < row; i++){
//            for(int j = 0; j < col; j++){
//                nums[i][j] = sc.nextInt();
//            }
//        }
//
//        for(int i = 0; i < col; i++){
//            for(int j = 0; j < row; j++){
//                System.out.print(nums[j][i]+" ");
//            }
//            System.out.println();
//        }

//        Scanner sc = new Scanner(System.in);
//        String strs = sc.nextLine();
//
//        char[] chars = strs.toCharArray();
//        List<String> nums = new ArrayList();
//        boolean flag = false;
//        int zero = 0;
//        int left = 0;
//        for(int i = 0; i < chars.length; i++){
//            if(chars[i] >= '0' && chars[i] <= '9'){
//                if(!flag){
//                    flag = true;
//                    if(chars[i] == '0'){
//                        ++zero;
//                    }else{
//                        zero <<= 1;
//                        left = i;
//                    }
//                }else{
//                    if(chars[i] == '0'){
//                        if(zero == 1){
//                            continue;
//                        }else{
//                            ++zero;
//                        }
//                    }
//                }
//            }else{
//                if(flag){
//                    if(zero == 1){
//                        nums.add("0");
//                    }else{
//                        nums.add(strs.substring(left, i));
//                    }
//                    zero = 0;
//
//                }
//            }
//        }
//        if(flag){
//            if(zero == )
//            nums.add(strs.substring(0, right));
//        }
//        String[] res = new String[nums.size()];
//        for(int i = 0; i < res.length; i++){
//            res[i] = nums.get(i);
//        }
//        Arrays.sort(res);
//        for(String n : res){
//            System.out.println(n);
//        }


    }


}
