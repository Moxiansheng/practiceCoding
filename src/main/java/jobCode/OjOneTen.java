package jobCode;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class OjOneTen {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        int i = 0;
        while(i < T){
            int j = 0, N = in.nextInt();
            int[] books = new int[N];
            while(j < N){
                books[j++] = in.nextInt();
            }
            int M = in.nextInt() - 2;
            Arrays.sort(books);
            int res = 0;
            for(int k = M; k < N - 1; k++){
                res += books[k];
            }
            System.out.println(res);
            i++;
        }

    }
}
