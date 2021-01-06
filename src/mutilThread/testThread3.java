package mutilThread;

public class testThread3 implements Runnable {
    private int tickets = 100;

    @Override
    public void run() {
        while(true){
            if(tickets <= 0){
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "No." + tickets--);
        }
    }
}
