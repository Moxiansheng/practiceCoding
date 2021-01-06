package mutilThread;

import network.client;

public class threadClient extends client implements Runnable{
    public threadClient(String ip, int port, String message) throws Exception{
        super(ip, port, message);
    }
    @Override
    public void run() {
        try {
            sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
