package network.TCP;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientServerTCP {
    private String ip = "127.0.0.1";
    private int port = 9999;
    private InetAddress serverIP = null;

    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private InputStream is = null;
    private OutputStream os = null;
    private FileInputStream fis = null;
    private FileOutputStream fos = null;
    private ByteArrayOutputStream baos = null;
    private ByteArrayInputStream bais = null;

    private String message = "";

    private static String received = "Received";
    private static String success = "Send Success";
    private static String failed = "Send Failed";

    public ClientServerTCP(int port){
        this.port = port;
    }

    public ClientServerTCP(String ip, int port) throws Exception{
        this.ip = ip;
        this.port = port;
    }

    // Client

    private void buildConnect() throws Exception{
        setServerIP();
        socket = new Socket(serverIP, port);
        os = socket.getOutputStream();
    }

    public void sendMessage() throws Exception{
        sendMessage(this.message);
    }

    public void sendMessage(String message) throws Exception{
        buildConnect();

        os.write(message.getBytes());

        socket.shutdownOutput();

        verifyReceived();

        closeConnect();
    }

    public void sendFile() throws Exception{
        sendFile(message);
    }

    public void sendFile(String fileName) throws Exception{
        buildConnect();

        fis = new FileInputStream(new File(fileName));
        byte[] buffer = new byte[1024];
        int len;
        while((len = fis.read(buffer)) != -1){
            os.write(buffer, 0, len);
        }

        socket.shutdownOutput();

        verifyReceived();

        closeConnect();
    }

    private void verifyReceived() throws Exception{
        is = socket.getInputStream();
        baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while((len = is.read(buffer)) != -1){
            baos.write(buffer, 0, len);
        }

        if(baos.toString().equals(received)){
            System.out.println(success);
        }else{
            System.out.println(failed);
        }
    }

    // Server

    public void openServer() throws Exception{
        setServerSocket();
        socket = serverSocket.accept();
    }

    public void getFile(String outputFilePath) throws Exception{
        openServer();
        is = socket.getInputStream();
        fos = new FileOutputStream(new File(outputFilePath));
        byte[] buffer = new byte[1024];
        int len;
        while((len = is.read(buffer)) != -1){
            fos.write(buffer, 0, len);
        }
        replyClient();
        closeConnect();
    }

    public String getMessage() throws Exception {
        openServer();
        // 初始设置port 10001
        System.out.println(serverSocket.getLocalPort()); // 监听port 10001
        System.out.println(socket.getLocalPort()); // 连接port 10001
        is = socket.getInputStream();
        baos = new ByteArrayOutputStream();
        String res = null;
        byte[] buffer = new byte[1024];
        int len;
        while((len = is.read(buffer)) != -1){
            baos.write(buffer, 0, len);
        }
        res = baos.toString();
        replyClient();
        closeConnect();
        return res;
    }

    private void replyClient() throws Exception{
        os = socket.getOutputStream();
        os.write(ClientServerTCP.received.getBytes());
    }

    private void closeConnect() throws Exception{
        if(bais != null){ bais.close(); }
        if(baos != null){ baos.close(); }
        if(fis != null){fis.close();}
        if(fos != null) { fos.close();}
        if(is != null){is.close();}
        if(os != null){os.close();}
        if(socket != null){socket.close();}
        if(serverSocket != null) serverSocket.close();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServerSocket() throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void setServerIP() throws Exception{
        this.serverIP = InetAddress.getByName(ip);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPort() {
        return port;
    }
}
