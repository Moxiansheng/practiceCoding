package mutilThread;

public class testThread2 implements Runnable{
    @Override
    public void run(){
        for (int i = 0; i < 20; i++) {
            System.out.println("他在看" + i);
        }
    }
}
