package mutilThread;

import network.TCP.ClientServerTCP;

public class ThreadServerTCP extends ClientServerTCP implements Runnable {
    public ThreadServerTCP(int port){
        super(port);
    }

    @Override
    public void run() {
        try {
            getFile("D:\\IDEA projects\\practiceCoding\\src\\resources\\received.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
