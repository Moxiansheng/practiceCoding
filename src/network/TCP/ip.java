package network.TCP;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ip {
    InetAddress ia;

    {
        try {
            ia = InetAddress.getByName("127.0.0.1");
            System.out.println(ia.getCanonicalHostName());
            ia = InetAddress.getByName("www.taobao.com");
            System.out.println(ia.getCanonicalHostName());
            System.out.println(ia.getHostAddress());
            System.out.println(ia.getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
