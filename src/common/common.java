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
}
