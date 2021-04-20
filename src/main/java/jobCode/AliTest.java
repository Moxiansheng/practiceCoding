package jobCode;

import java.util.ArrayList;
import java.util.List;

public class AliTest {
    public static void main(String[] args) {

    }
}


/**
// 序列化与反序列化
//public class AliTest {
//    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
//        list.add("");
//        list.add("abc");
//        list.add("3#abc");
//        list.add("5#3#abc");
//
//        System.out.println("=====before=====");
//        list.forEach(System.out::println);
//
//        String encoded = encode(list);
//
//        System.out.println("=====encoded=====");
//        System.out.println(encoded);
//
//        List<String> decoded = decode(encoded);
//
//        System.out.println("=====decoded=====");
//        decoded.forEach(System.out::println);
//    }
//
//    public static String encode(List<String> list){
//        StringBuilder sb = new StringBuilder();
//        for(String str : list){
//            sb.append(str.length()+"#"+str);
//        }
//        return sb.toString();
//    }
//
//    public static List<String> decode(String str){
//        List<String> res = new ArrayList<>();
//        do{
//            int split = str.indexOf('#') + 1;
//            int len = Integer.valueOf(str.substring(0, split - 1));
//            res.add(str.substring(split, split+len));
//            str = str.substring(split+len);
//        }while(str != null && str.length() > 0);
//        return res;
//    }
//}
*/
