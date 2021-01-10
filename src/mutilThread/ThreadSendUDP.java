package mutilThread;

import network.UDP.SendReceiveUDP;

public class ThreadSendUDP extends SendReceiveUDP implements Runnable {
    public ThreadSendUDP(int fromPort, String ip, int toPort){
        super(fromPort, ip, toPort);
    }

    @Override
    public void run() {
        try {
            sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String ip = "localhost";
        String id = "Big Mo";
        int toPort = 9070;
        int fromPort = 9081;
        int receivePort = 9090;

        new Thread(new ThreadSendUDP(fromPort, ip, toPort)).start();
        new Thread(new ThreadReceiveUDP(receivePort, id)).start();
    }
}
