package mutilThread;


import network.TCP.ClientServerTCP;

public class ThreadClientTCP extends ClientServerTCP implements Runnable{
    public ThreadClientTCP(String ip, int port, String message) throws Exception{
        super(ip, port);
        setMessage(message);
    }
    @Override
    public void run() {
        try {
            sendFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
