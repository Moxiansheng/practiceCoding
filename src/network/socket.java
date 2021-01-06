package network;

import java.net.InetSocketAddress;

public class socket {
    public socket(){
        InetSocketAddress sa = new InetSocketAddress("127.0.0.1", 8080);
        System.out.println(sa);
        System.out.println(sa.getAddress());
        System.out.println(sa.getHostName());
        System.out.println(sa.getPort());
    }
}
