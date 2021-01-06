package mutilThread;

import network.server;

import java.io.IOException;

public class threadServer extends server implements Runnable {
    public threadServer(int port) throws Exception{
        super(port);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5);
            String message = getMessage();
            System.out.println("Receive: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
