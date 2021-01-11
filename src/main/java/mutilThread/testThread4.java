package mutilThread;

import org.omg.PortableServer.THREAD_POLICY_ID;

public class testThread4 implements Runnable{
    private static int goal = 100;
    private static String winner;

    @Override
    public void run() {
        for (int meter = 0; meter <= goal; meter++) {
            if(Thread.currentThread().getName() == "rabbit"){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(hasSomebodyReach(meter)){
                break;
            }
            System.out.println(Thread.currentThread().getName() + ": " + meter);
        }
    }

    private boolean hasSomebodyReach(int meter){
        if(winner != null){
            return true;
        }{
            if (meter >= goal) {
                winner = Thread.currentThread().getName();
                System.out.println("winner: " + winner);
                return true;
            }
            return false;
        }
    }
}
