import designPattern.Proxy.*;
import lambda.*;
import mutilThread.*;
import network.*;
import reflection.*;
import sort.*;
import dataStructure.*;
import hashCode.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class test {
    private static int type = 9;
    /*
        0: sort
        1: dataStructure
        2: reflection
        3: equals()
        4: hashCode()
        5: 判断质数算法，哪个更快
        6. network
        7. multiThread
        8. designPattern
        9. lambda
    */

    public static void main(String[] args) throws Exception{
        //Scanner sc = new Scanner(System.in);
        // type = sc.nextInt();
        test();
    }

    private static void test() throws Exception{
        if(type == 0){
            test0();
        }else if(type == 1){
            test1();
        }else if(type == 2){
            test2();
        }else if(type == 3){
            test3();
        }else if(type == 4){
            test4();
        }else if(type == 5){
            test5();
        }else if(type == 6){
            test6();
        }else if(type == 7){
            test7();
        }else if(type == 8){
            test8();
        }else if(type == 9){
            test9();
        }
    }

    private static void test0(){
        int[] array = {22, 34, 3, -32, 82, 55, 89, -50, 37, -5, 64, 35, 9, 70};
        base sort = new bucket(array, false, 2);
        sort.sort();
        sort.show();
    }

    private static void test1(){
        int[] array = {22, 34, 3, 32, 82, 55, 89, 50, 37, 5, 64, 35, 9, 70};
        heap h = new heap(array, false);
        h.show();
        h.remove();
        h.show();
        h.removeByVal(50);
        h.show();
        h.removeByIndex(4);
        h.show();
        h = new heap();
        h.show();
        h.removeByIndex(0);
    }

    private static void test2() throws Exception{
        String classPath = "reflection.reflectedClass";
        reflectClass.reflectNewInstance(classPath);
        reflectClass.reflectPrivateConstructor(classPath);
        reflectClass.reflectPrivateField(classPath);
        reflectClass.reflectPrivateMethod(classPath);

        reflectedClass rc = reflectClass.reflectNewInstanceT(reflectedClass.class);
        rc.setName(classPath);
        rc.setAuthor(classPath);
        System.out.println(rc.toString());
    }

    private static void test3() throws Exception{
        String a = "ab";
        String b = "ab";
        System.out.println(a.equals(b));

        reflectedClass rc1 = reflectClass.reflectNewInstanceB(reflectedClass.class, a, b);
        reflectedClass rc2 = reflectClass.reflectNewInstanceT(reflectedClass.class);
        rc2.setName(a);
        rc2.setAuthor(b);
        System.out.println(rc1.equals(rc2));
    }

    private static void test4(){
        singleIntSet set;
        int times = 1;

        common.common.intervalLine(times++);
        set = new singleIntSet();
        set.Add(3);
        set.Add(7);
        System.out.println(set.Contains(3)); // 输出 true
        System.out.println(set.Contains(5)); // 输出 false

        common.common.intervalLine(times++);

        set = new singleIntSet2();
        set.Add(13);
        set.Add(17);
        System.out.println(set.Contains(13)); // 输出 true
        System.out.println(set.Contains(15)); // 输出 false

        common.common.intervalLine(times++);

        set = new singleIntSet3();
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        System.out.println(set.Contains(3));

        common.common.intervalLine(times++);

        set = new singleIntSet4();
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        System.out.println(set.Contains(3));

        common.common.intervalLine(times++);

        set = new singleIntSet5();
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        System.out.println(set.Contains(3));

        common.common.intervalLine(times++);

        set = new singleIntSet6(9);
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        System.out.println(set.Contains(3));

        common.common.intervalLine(times++);

        set = new singleIntSet7(9);
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        System.out.println(set.Contains(3));
    }

    private static void test5(){
        long start = System.nanoTime();
        common.common.isPrime1(999999999);
        long end = System.nanoTime();
        System.out.println(end - start);

        common.common.intervalLine(0);

        start = System.nanoTime();
        common.common.isPrime2(999999999);
        end = System.nanoTime();
        System.out.println(end - start);
    }

    private static void test6() throws Exception{
        // ip i = new ip();
        // socket s = new socket();
        String ipAdd = "127.0.0.1";
        int port = 9999;
        String sendMessage = "Hello, world!";

        server s = new server(port);

        client c = new client(ipAdd, port, sendMessage);
        c.sendMessage();

        String getMessage = s.getMessage();
        System.out.println(getMessage);
    }

    private static void test7() throws Exception{
        // Thread
//            testThread tt = new testThread();
//            tt.start();
//            for (int i = 0; i < 1000; i++) {
//                System.out.println("我也再看" + i);
//            }

        // Runnable
//            testThread2 tt2 = new testThread2();
//            new Thread(tt2).start();

        // 买票
//            testThread3 tt3 = new testThread3();
//            new Thread(tt3).start();
//            new Thread(tt3).start();
//            new Thread(tt3).start();

        // 龟兔赛跑
//            testThread4 tt4 = new testThread4();
//            new Thread(tt4, "rabbit").start();
//            new Thread(tt4, "turtle").start();

        // cs-socket通讯
//            String ipAdd = "127.0.0.1";
//            int port = 9999;
//            String sendMessage = "Hello, world!";
//
//            threadServer threadServer = new threadServer(port);
//            new Thread(threadServer).start();
//
//            threadClient threadClient = new threadClient(ipAdd, port, sendMessage);
//            new Thread(threadClient).start();

        // Callable
        testThread5 tt5 = new testThread5(1);

        // 创建执行服务
        ExecutorService es = Executors.newFixedThreadPool(3);

        // 提交执行
        Future<Integer> r1 = es.submit(tt5);
        Future<Integer> r2 = es.submit(tt5);
        Future<Integer> r3 = es.submit(tt5);

        //获取结果
        System.out.println(r1.get());
        System.out.println(r2.get());
        System.out.println(r3.get());

        //关闭服务
        es.shutdown();
    }

    private static void test8(){
        //static proxy
        staticProxy proxy = new staticProxy(new agent());
        proxy.happyMarry();
    }

    private static void test9(){
        int a = -1;
        lambda lambda;

//        // 1.正常实现
//        lambda = new testLambda();
//        lambda.lambda(a);
//
//        // 2. 局部内部类
//        class testLambda2 implements lambda{
//
//            @Override
//            public void lambda(int a) {
//                System.out.println("lambda study 2" + a);
//            }
//        }
//        lambda = new testLambda2();
//        lambda.lambda(a);
//
//        // 3. 匿名内部类
//        lambda = new lambda() {
//            @Override
//            public void lambda(int a) {
//                System.out.println("lambda study 3" + a);
//            }
//        };
//        lambda.lambda(a);

        // 4. lambda
        lambda = (int z)->{
            System.out.println("lambda study 4" + z);
            System.out.println("is: " + z);
        };
        lambda.lambda(a);

        // 5. lambda简化 去参数类型（多参数也可以，但要共进退）
        lambda = (z)->{
            System.out.println("lambda study 5" + z);
            System.out.println("is: " + z);
        };
        lambda.lambda(a);

        // 6. lambda简化 去括号（单参数才可以去括号）
        lambda = z->{
            System.out.println("lambda study 6" + z);
            System.out.println("is: " + z);
        };
        lambda.lambda(a);
    }
}