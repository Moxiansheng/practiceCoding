package common;

import java.util.Arrays;

public class common {
    public static int[] swap(int[] arr, int a, int b, int min, int max){
        int[] array = Arrays.copyOf(arr, arr.length);
        if(a != b && a <= max && b <= max && a >= min && b >= min){
            int temp = array[a];
            array[a] = array[b];
            array[b] = temp;
        }
        return array;
    }

    public static boolean isPrime1(int candidate) {
        /*
         * 较慢的判断质数算法
         * */
        if ((candidate & 1) != 0) // 是奇数
        {
            int limit = (int)Math.sqrt(candidate);
            for (int divisor = 3; divisor <= limit; divisor += 2) // divisor = 3、5、7...candidate的平方根
            {
                if ((candidate % divisor) == 0)
                    return false;
            }
            return true;
        }
        return (candidate == 2); // 除了2，其它偶数全都不是质数
    }

    public static boolean isPrime2(int num) {
        /*
        * 更快的判断质数算法
        * */
        if (num <= 3) {
            return num > 1;
        }
        // 不在6的倍数两侧的一定不是质数
        if (num % 6 != 1 && num % 6 != 5) {
            return false;
        }
        int sqrt = (int) Math.sqrt(num);
        for (int i = 5; i <= sqrt; i += 6) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}
