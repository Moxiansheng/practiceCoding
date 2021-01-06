package network;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class client {
    String ip = "127.0.0.1";
    int port = 9999;
    InetAddress serverIP = null;
    Socket socket = null;
    OutputStream os = null;

    String message = "";

    public client(String ip, int port, String message) throws Exception{
        this.ip = ip;
        this.port = port;
        this.message = message;
    }

    public void sendMessage() throws Exception{
        setServerIP();
        socket = new Socket(serverIP, port);
        OutputStream os = socket.getOutputStream();
        os.write(message.getBytes());
        os.close();
        socket.close();
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setServerIP() throws Exception{
        this.serverIP = InetAddress.getByName(ip);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
