package mutilThread;

public class testThread extends Thread {
    @Override
    public void run(){
        for (int i = 0; i < 20; i++) {
            System.out.println("我在看" + i);
        }
    }
}
