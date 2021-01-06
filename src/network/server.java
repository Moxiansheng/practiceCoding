package network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class server{
    private int port = 9999;
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private InputStream is = null;
    private ByteArrayOutputStream baos = null;

    public server(int port){
        this.port = port;
    }

    public String getMessage() throws IOException{
        setServerSocket();
        socket = serverSocket.accept();
        is = socket.getInputStream();
        baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while((len = is.read(buffer)) != -1){
            baos.write(buffer, 0, len);
        }
        String message = baos.toString();
        baos.close();
        is.close();
        socket.close();
        serverSocket.close();
        return message;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setServerSocket() throws IOException {
        this.serverSocket = new ServerSocket(port);
    }
}
