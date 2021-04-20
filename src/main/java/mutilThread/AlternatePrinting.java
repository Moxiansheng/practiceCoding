package mutilThread;

import java.util.concurrent.atomic.AtomicInteger;

public class AlternatePrinting {
    static class IncrementInteger{
        public static volatile boolean flag = false;
        int j = 1;
        char c = 'A';
        public void product(){
            for (int i = 0; i < 26;) {
                if(!flag){
                    synchronized(this){
                        if(!flag){
                            System.out.println("1:" + j++);
                            System.out.println("1:" + j++);
                            i++;
                            flag = true;
                        }
                    }
                }

            }
        }

        public void custom(){
            for (int i = 0; i < 26;) {
                if(flag){
                    synchronized(this){
                        if(flag){
                            System.out.println("2:" + c++);
                            i++;
                            flag = false;
                        }
                    }
                }

            }
        }
    }
    public static void main(String[] args){
        IncrementInteger incrementInteger = new IncrementInteger();
        new Thread(() -> {
            incrementInteger.product();
        }).start();
        new Thread(() -> {
            incrementInteger.custom();
        }).start();
    }
}
