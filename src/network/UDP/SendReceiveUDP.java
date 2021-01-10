package network.UDP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class SendReceiveUDP {
    private String ip = "";
    private int toPort;
    private int fromPort;
    private String id = "";

    private InetAddress ia = null;

    private DatagramSocket ds = null;
    private DatagramPacket dp = null;

    BufferedReader reader = null;

    private String message = "";
    private String breakMsg = "bye";

    public SendReceiveUDP(){

    }

    public SendReceiveUDP(String ip , int toPort){
        this.toPort = toPort;
        this.ip = ip;
        try {
            ds = new DatagramSocket(fromPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SendReceiveUDP(int fromPort, String id){
        this.fromPort = fromPort;
        this.id = id;
        try {
            ds = new DatagramSocket(fromPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SendReceiveUDP(int fromPort, String ip, int toPort){
        this.ip = ip;
        this.toPort = toPort;
        this.fromPort = fromPort;

        try {
            ds = new DatagramSocket(fromPort);
            reader = new BufferedReader(new InputStreamReader(System.in));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Send
    public void sendMessage() throws Exception{
        sendMessage(message);
    }

    public void sendMessage(String message) throws Exception{
        while(true){
            String data = reader.readLine();
            if(data.equals(breakMsg)){
                break;
            }
            byte[] buffer = data.getBytes();
            dp = new DatagramPacket(buffer, 0, buffer.length, new InetSocketAddress(ip, toPort));
            ds.send(dp);
        }
        ds.close();
    }


    // Receive
    public String getMessage() throws Exception{
        StringBuffer res = new StringBuffer();
        while(true){
            byte[] buffer = new byte[1024];
            dp = new DatagramPacket(buffer, 0, buffer.length);
            ds.receive(dp); // 阻塞式接收
            byte[] d = dp.getData();
            String data = new String(d, 0, d.length);
            res.append(data);
            System.out.println(id + ":" + data);
            if(data.equals(breakMsg)){
                System.out.println("break");
                break;
            }
        }
        ds.close();
        return res.toString();
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
