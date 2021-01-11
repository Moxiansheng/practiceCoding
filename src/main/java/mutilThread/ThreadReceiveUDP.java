package mutilThread;


import network.UDP.SendReceiveUDP;

public class ThreadReceiveUDP extends SendReceiveUDP implements Runnable {
    public ThreadReceiveUDP(int port, String id){
        super(port, id);
    }

    @Override
    public void run() {
        try {
            String receive = getMessage();
            System.out.println(receive);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String ip = "localhost";
        String id = "Small Mo";
        int toPort = 9090;
        int fromPort = 9080;
        int receivePort = 9070;

        new Thread(new ThreadSendUDP(fromPort, ip, toPort)).start();
        new Thread(new ThreadReceiveUDP(receivePort, id)).start();
    }
}
