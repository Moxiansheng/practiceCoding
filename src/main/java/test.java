import DeepCopy.CloneMethod.AddressM2;
import DeepCopy.CloneMethod.UserM2;
import DeepCopy.ConstructorMethod.AddressM1;
import DeepCopy.ConstructorMethod.UserM1;
import DeepCopy.SerializableMethod.AddressM3;
import DeepCopy.SerializableMethod.UserM3;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import common.*;
import designPattern.Proxy.*;
import generic.*;
import lambda.*;
import mutilThread.*;
import network.TCP.ClientServerTCP;
import network.UDP.SendReceiveUDP;
import network.URL.URLDownloader;
import org.apache.commons.lang3.SerializationUtils;
import reflection.*;
import sort.*;
import dataStructure.*;
import hashCode.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class test {
    private static int type = 18;
    /*
        0:  sort
        1:  heap
        2:  reflection
        3:  equals()
        4:  hashCode()
        5:  判断质数算法，哪个更快
        6.  TCP
        7.  multiThread
        8.  designPattern
        9.  lambda
        10. generic
        11. Inversion and Covariance 逆变与协变
        12. 关于null
        13. 拆箱与装箱
        14. LinkedList
        15. UDP
        16. URL
        17. 值传递与引用传递
        18. 深copy与浅copy
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
        }else if(type == 10){
            test10();
        }else if(type == 11){
            test11();
        }else if(type == 12){
            test12();
        }else if(type == 13){
            test13();
        }else if(type == 14){
            test14();
        }else if(type == 15){
            test15();
        }else if(type == 16){
            test16();
        }else if(type == 17){
            test17();
        }else if(type == 18){
            test18();
        }
    }

    private static void test18() throws Exception{
        String city = "nj";
        String country = "China";
        String name = "mo";
        String newCity = "zz";

        // Method1 Constructor
        UserM1 userM1 = new UserM1(name, new AddressM1(city, country));

        UserM1 userM1C = new UserM1(userM1.getName(), new AddressM1(userM1.getAddress().getCity(), userM1.getAddress().getCountry()));

        userM1C.getAddress().setCity(newCity);
        System.out.println(userM1.getAddress().getCity() + " : " + userM1C.getAddress().getCity());

        /**
         *   Method2 Override Clone()
         *   Object父类有个clone()的拷贝方法，不过它是protected类型的，我们需要重写它并修改为public类型。
         *   除此之外，子类还需要实现Cloneable接口来告诉JVM这个类是可以拷贝的。
         */
        UserM2 userM2 = new UserM2(name, new AddressM2(city, country));

        UserM2 userM2C = userM2.clone();

        userM2C.getAddress().setCity(newCity);
        System.out.println(userM2.getAddress().getCity() + " : " + userM2C.getAddress().getCity());
        userM2C.setName(newCity);
        System.out.println(userM2.getName() + " : " + userM2C.getName());

        /**
         *          Apache Commons Lang序列化
         *          Java提供了序列化的能力，我们可以先将源对象进行序列化，再反序列化生成拷贝对象。
         *          但是，使用序列化的前提是拷贝的类（包括其成员变量）需要实现Serializable接口。
         *          Apache Commons Lang包对Java序列化进行了封装，我们可以直接使用它
         */

        UserM3 userM3 = new UserM3(name, new AddressM3(city, country));

        UserM3 userM3C = SerializationUtils.clone(userM3);

        userM3C.getAddress().setCity(newCity);
        System.out.println(userM3.getAddress().getCity() + " : " + userM3C.getAddress().getCity());
        userM3C.setName(newCity);
        System.out.println(userM3.getName() + " : " + userM3C.getName());

        /**
         *          Gson序列化
         *          Gson可以将对象序列化成JSON，也可以将JSON反序列化成对象，所以我们可以用它进行深拷贝。
         */

        Gson gson = new Gson();
        UserM1 userM4C = gson.fromJson(gson.toJson(userM1), UserM1.class);

        userM4C.getAddress().setCity(newCity);
        System.out.println(userM1.getAddress().getCity() + " : " + userM4C.getAddress().getCity());
        userM4C.setName(newCity);
        System.out.println(userM1.getName() + " : " + userM4C.getName());

        /**
         *          Jackson序列化
         *          与Gson相似，可以将对象序列化成JSON，明显不同的地方是
         *          拷贝的类（包括其成员变量）需要有默认的无参构造函数
         */

        ObjectMapper objectMapper = new ObjectMapper();
        UserM1 userM5C = objectMapper.readValue(objectMapper.writeValueAsString(userM1), UserM1.class);

        userM5C.getAddress().setCity(newCity);
        System.out.println(userM1.getAddress().getCity() + " : " + userM5C.getAddress().getCity());
        userM5C.setName(newCity);
        System.out.println(userM1.getName() + " : " + userM5C.getName());
    }

    private static void test17() {
        // 这种交换是交换的copy的引用，也就是说，原始值的地址指向没有变
        // 只是拷贝出来的参数的指向发生了交换，影响不到原有值
        // 但如果是对值进行修改，那么就可以影响到外部了，也就是函数外
        // 因为指向的是同一片空间，一方对空间修改，另一方自然也受影响
        String s1 = "a";
        String s2 = "b";
        common.swapString(s1, s2);
        System.out.println(s1);
        System.out.println(s2);
    }

    private static void test16() throws Exception{
        String url = "https://m801.music.126.net/20210110232511/4f3468a771c1967dcff210da10e462ac/jdyyaac/obj/w5rDlsOJwrLDjj7CmsOj/5640428920/e99c/9015/2057/d7fa4163b75631c062223dbe42b76218.m4a";
        new URLDownloader(url).download();
    }

    private static void test15() throws Exception{
        // 用多线程才能测出来
        String ipAdd = "127.0.0.1";
        int port = 9999;
        String sendMessage = "D:\\IDEA projects\\practiceCoding\\src\\resources\\photo.jpg";

        SendReceiveUDP sudp = new SendReceiveUDP(ipAdd, port);
        sudp.sendMessage();
    }

    private static void test14() {
        SingleLinkedList<Double> sll = new SingleLinkedList<Double>();
        System.out.println(sll.addAtHead(2.1));
        System.out.println(sll.addAtTail(3.2));
        System.out.println(sll.addAtIndex(2.9, 1));
        System.out.println(sll.get(-1));
        System.out.println(sll.get(3));
        System.out.println(sll.get(0));
        System.out.println(sll.delIndex(-1));
        System.out.println(sll.get(0));
        System.out.println(sll.getHead());
        System.out.println(sll.delIndex(3));
        System.out.println(sll.getTail());
    }

    private static void test13() {
        // 简单一点说，装箱就是自动将基本数据类型转换为包装器类型；
        // 拆箱就是自动将包装器类型转换为基本数据类型。
        // Autoboxing
        Integer total = 99;

        // Unboxing
        int totalPrim = total;

        /**
         * 若进行反编译可以得到
         * Integer total = 99;
         * 执行上面那句代码的时候，系统为我们执行了：
         * Integer total = Integer.valueOf(99);
         *
         * int totalPrim = total;
         * 执行上面那句代码的时候，系统为我们执行了：
         * int totalPrim = total.intValue();
         *
         * 进Integer的源码进行查阅
         */

        //// 比较对比装箱操作是否更费时
        // 装箱数在IntegerCache.low与IntegerCache.high时
//        long sum1 = 0;
//        long sum2 = 0;
//        int num = 0;
//        double times = 100000000;
//        for (int k = 0; k < times; k++) {
//            long start1 = System.nanoTime();
//            Integer i = new Integer(num);
//            long end1 = System.nanoTime();
//            sum1 += end1 - start1;
//
//            long start2 = System.nanoTime();
//            Integer j = num;
//            long end2 = System.nanoTime();
//            sum2 += end2 - start2;
//        }
//        System.out.println(sum1 / times); // 慢
//        System.out.println(sum2 / times); // 快
        // 最终结果装箱操作更加快
        // =================================================
        // 装箱数不在IntegerCache.low与IntegerCache.high时
//        long sum1 = 0;
//        long sum2 = 0;
//        int num = 200;
//        double times = 100000000;
//        for (int k = 0; k < times; k++) {
//            long start1 = System.nanoTime();
//            Integer i = new Integer(num);
//            long end1 = System.nanoTime();
//            sum1 += end1 - start1;
//
//            long start2 = System.nanoTime();
//            Integer j = num;
//            long end2 = System.nanoTime();
//            sum2 += end2 - start2;
//        }
//        System.out.println(sum1 / times); // 慢
//        System.out.println(sum2 / times); // 快
        // 最终结果装箱操作更加快
        // 执行次数足够多时，范围内十分接近，范围外较为接近

        Integer i1 = 100;
        Integer i2 = 100;
        Integer i3 = 200;
        Integer i4 = 200;
        System.out.println(i1 == i2); // true
        System.out.println(i3 == i4); // false
        // 因为Integer范围内指向同一个对象，范围外新建对象

        Double d5 = 100.0;
        Double d6 = 100.0;
        System.out.println(d5 == d6); // false
        // 因为Double每次都新建对象

        /**
         * 总结：
         * Integer, Byte, Character, Long这几个类的valueOf方法类似
         * Double, Float的valueOf方法类似
         */

        // 两个静态对象, 类似单例
        Boolean b1 = false;
        Boolean b2 = false;
        Boolean b3 = true;
        Boolean b4 = true;
        System.out.println((b1 == b2)); // true
        System.out.println((b3 == b4)); // true

        //// == 与 equals的拆装箱操作 int没有equals函数
        Integer num1 = 200;
        int num2 = 200;
        System.out.println(num1 == num2); // true
        // 只能是num1进行了拆箱操作，之后按两个int进行比较
        // 因为如果是num2装箱，因为在范围外，就会产生新对象
        // 按两个对象进行比较的话，就是不同的对象，为false

        Integer num3 = 100;
        int num4 = 100;
        System.out.println(num3.equals(num4));  //true

        Integer num5 = 200;
        int num6 = 200;
        Long num7 = 400l;
        System.out.println(num7 == (num5 + num6));  //true
        System.out.println(num7.equals(num5 + num6)); //false
        // ==时为真，是因为num7拆箱拆成了400
        // num5要进行+运算，被先拆成了200，之后和200相加，是int型400
        // equals为假，是因为Long的equals函数要求，必须类型和值都相等时，
        // 才判断为真，这里是因为类型不同，传进去的是int

        /**
         * 总结：
         * 当 “==”运算符的两个操作数都是包装器类型的引用时，
         * 是比较指向的是否是同一个对象，而如果其中有一个操作数是
         * 表达式（即包含算术运算）则比较的是数值（即会触发自动拆箱的过程）
         */

        //// 八种包装器类型equals函数查看和valueOf函数查看
        Short s1 = 5;
        Byte bt1 = 5;
        Integer it1 = 5;
        Long lg1 = 5l;
        Float f1 = 5f;
        Double db1 = 5d;
        Character ch1 = 5;
        Boolean bl1 = false;

        // 八种equals函数都包含有类型判断，即，若类型不匹配则为false
        // equals : type == type && value == value
//        bt1.equals();
//        s1.equals();
//        it1.equals();
//        lg1.equals();
//        f1.equals();
//        db1.equals();
//        ch1.equals();
//        bl1.equals();

        // valueOf函数中Short，Byte，Integer，Long都是[-128，127]的缓存
        // 但其中Integer的最大范围并没有定死，可通过继承重写，其余的则已写死
        // Character也是缓存的常量池，但是范围是[0, 127]
        // Float与Double都是新建对象，没有缓存常量池
        // Boolean是两个静态对象，返回其中之一
        System.out.println(Short.valueOf(s1));
        System.out.println(Byte.valueOf(bt1));
        System.out.println(Integer.valueOf(it1));
        System.out.println(Long.valueOf(lg1));
        System.out.println(Float.valueOf(f1));
        System.out.println(Double.valueOf(db1));
        System.out.println(Character.valueOf(ch1));
        System.out.println(Boolean.valueOf(bl1));
    }

    private static void test12() {
        // 1. null对象拆箱，会出现控制针错误
//        Integer obj = null;
//        int i = obj; // 这一步进行了拆箱操作，会报空指针错误
//        System.out.println(i);

        // 2. null既不是对象也不是一种类型，它仅是一种特殊的值，
        // 你可以将其赋予任何引用类型，你也可以将null转化成任何类型
        String str = null;
        Integer itr = null;
        String str2 = (String) null;
        Integer itr2 = (Integer) null;

        // 3. null不能给基本类型变量赋值
//        int i = null; // 报错

        // 4. 如果使用了带有null值的引用类型的变量，
        // instanceof操作会返回false
        Integer integer = null;
        if(integer instanceof Integer){
            System.out.println("Yes");
        }else{
            System.out.println("No");
        }

        // 5. 可以在非基本类型变量参数处传入null，类似将null赋值给非基本类型变量
        // 不能把null传入基本类型变量参数，类似于不能将null赋值给基本类型变量
        Integer ints = null;
//        common.common.intervalLine(null); // 基本类型参数，报错

        // 6. Java中null == null为true
        if(null == null){
            System.out.println("Yes equal null");
        }else{
            System.out.println("No equal null");
        }
    }

    private static void test11(){
        // 一般对象和数组赋值时可以协变
        Number num = new Integer(1);
        // 赋值时不可以逆变
//        num = new Object(); // false

        class plant{

        }

        class Fruit extends plant{
            public Number fun(Number o){
                return 0;
            }
        }

        class Apple extends Fruit{
            // 返回值可以协变
            @Override
            public Integer fun(Number o){
                return o.intValue();
            }

            // 返回值不能逆变 报错
//            @Override
//            public Object fun(Number o){
//                return o;
//            }


            // 参数（输入值）可以协变，
            // 但是被认为是重载，
            // 而非重写，
            // 故不能带@Override
//            @Override
            public Number fun(Integer o){
                return o;
            }

            // 参数（输入值）可以逆变，
            // 但是被认为是重载，
            // 而非重写，
            // 故不能带@Override
//            @Override
            public Number fun(Object o){
                return 0; // 这里看作是赋值的协变，即Number resources = 0;return resources;
            }

            /**
             * 上两种总结起来就是，不能实现参数值的协变与逆变
             * 强行实现，只能算作重载。
             */
        }

        class redApple extends Apple{

        }

        class smallRedApple extends redApple{

        }

        Fruit f = new Fruit();
        // 左边引用的类型是右边函数返回值类型的基类型，算是返回值赋值的协变
        Object obj = f.fun(0); // correct
        // 左边引用的类型是右边函数返回值类型的子类型，返回值赋值时不能逆变
//        Integer itg = f.fun(0); // error
        // 函数传入值是右边函数参数类型的基类不可行,即参数赋值不能逆变
//        Number num1 = f.fun(new Object()); // error
        // 函数传入值是右边函数参数类型的子类可行,即参数不能协变,算是参数赋值协变
        Number num1 = f.fun(new Integer(0)); // error

        /**
         * 综上都算是赋值时的协变,符合里氏代换原则,赋值仍是不可逆变
         */

        f = new Apple();
        Number o = f.fun(0);
//        o = f.fun(new Object());

        /**
         * o = f.fun(new Object());
         * 无法编译通过，即Apple里的重载方法无法被执行到，
         * 主要是因为f被声明为了Fruit，在指向Apple后，
         * 再调用f的方法时，会进入到Apple重写的fun函数中，
         * 也就是Fruit所有的方法,而不是Apple重载的函数fun
         * 因为重载方法对于Fruit而言相当于不存在
         */

        Apple a = new Apple();
        Number number = 0;
        o = a.fun(number);
        o = a.fun(0);
        o = a.fun(new Object());

        /**
         * 只有将其声明为Apple后,才能执行到重载函数
         */

        // 列表不能协变也不能逆变，即为不变
//        ArrayList<Number> list = new ArrayList<Integer>(); // false
//        ArrayList<Integer> list = new ArrayList<Number>(); // false

        /**
         *
         * 如果用-∞到Number表示所有Number的父类即(-∞, Number)
         * 用Number到∞表示所有Number的子类(Number, ∞)
         *
         * 当使用? extends Number时，这里的通配符?就表示为了
         * 所有Number的子类，即范围(Number, ∞)
         * 而由于参数必须是该范围内所有类的子类才可以被传入，
         * （这是因为只有这样，参数才满足里氏代换原则）
         * 对于任一Number子类α∈(Number, ∞)
         * 无法确定它是范围内所有类的子类
         * 用数字举例：
         * 在范围(0, ∞)中的任一数字β，
         * 无法使得β小于范围(0, ∞)的所有数字
         * 那也就意味着,什么都无法传入
         * 即 nothing 可传入
         *
         * 当使用? super Number时，这里的通配符?就表示为了
         * 所有Number的父类，即范围(-∞, Number)
         * 由于参数必须是该范围内所有类的子类才可以被传入，
         * （这是因为只有这样，参数才满足里氏代换原则）
         * 对于任一Number子类β∈(Number, ∞)
         * 可以确定它是范围内所有类的子类
         * 用数字举例：
         * 在范围(0, ∞)中的任一数字β，
         * 都可以使得β小于范围(-∞, 0)的所有数字
         * 但是Number的任一父类也都无法传入了
         * 即 [Number, ∞) 可传入
         *
         */

        // 不可变之初始化无法协变
//        List<Fruit> list1 = new ArrayList<Apple>(); // error

        // 通过? extends Number使其能初始化协变
        ArrayList<Apple> list1 = new ArrayList<Apple>();
        // 传入父类 逆变了 不能逆变
//        list1.add(new Fruit()); // error
        list1.add(new redApple());
        ArrayList<? extends Fruit> list2 = list1; // correct
        // 传入父类
//        list2.add(new Object()); // error
//        list2.add(new plant()); // error
        // 传入自己
//        list2.add(new Fruit()); // error
        // 传入子类
//        list2.add(new Apple()); // error
//        list2.add(new smallRedApple()); // error
        // 什么都传不进去了
        Object o1 = list2.get(0); // 赋值协变
        plant p1 = list2.get(0); // 赋值协变
        Fruit f1 = list2.get(0); // 正常赋值
//        Apple a2 = list2.get(0); // error 赋值不能逆变
        System.out.println(" " + o1.getClass() + " ");
        System.out.println(" " + p1.getClass() + " ");
        System.out.println(" " + f1.getClass() + " ");

        /**
         * ? extends Fruit使其在初始化时,可以协变,
         * 即可以指向以其子类Apple,也就是extends Fruit的类,初始化的列表
         *
         * input:nothing
         * inner:[Number, ∞)
         * return:[Number]
         */

        ArrayList<Apple> list3 = new ArrayList<Apple>();
        // 算是参数赋值协变
        list3.add(new Apple());
        list3.add(new redApple());
        // 通过? super Number使其能初始化逆变
        List<? super redApple> list4 = list3;
        System.out.println(list4.getClass());
        // 传自己
        list4.add(new redApple()); // correct
        // 传父类
//        list4.add(new Apple()); // error
//        list4.add(new Object()); // error
        // 传子类
        list4.add(new smallRedApple());
        // 啥也返回不出了
//        plant p2 = list4.get(0); // error
//        Fruit f2 = list4.get(0); // error
//        Apple a2 = list4.get(0); // error
//        redApple ra2 = list4.get(0); // error
//        smallRedApple sra2 = list4.get(0); // error
        Object obj1 = list4.get(1);
        System.out.println(obj1.getClass());
        /**
         * ? super redApple使其在初始化时,可以逆变,
         * 即可以指向以其父类Apple,也就是super redApple的类,初始化的列表
         *
         * input:[Number, ∞)
         * inner:[Number, ∞)
         * return:need to cast
         */

        /**
         * 其实无论是? extends还是? super
         * 都只是让ArrayList的赋值增加协变和逆变性
         *
         * 如果协变了,就啥也加不进去了,但是可以返回成它的父类,好比进行了赋值协变
         *
         * 如果逆变了,虽然可以继续加入自己和子类,但是无法直接返回了
         */

    }

    private static void test10() throws Exception{
        // Java泛型

        // Java类型擦除
        // E1.原始类型相等
        String a = "abc";
        int b = 123;

        ArrayList<String> list1 = new ArrayList<String>();
        list1.add(a);

        ArrayList<Integer> list2 = new ArrayList<Integer>();
        list2.add(b);

        System.out.println(list1.getClass() == list2.getClass());

        // ========================================================
        common.intervalLine2();

        // E2.通过反射添加其他类型元素
        ArrayList<Integer> list3 = new ArrayList<Integer>();
        list3.add(b);

        list3.getClass().getMethod("add", Object.class).invoke(list3, a);
        for (int i = 0; i < list3.size(); i++) {
            System.out.println(list3.get(i));
        }
        System.out.println(list3.getClass());
        // 要区分原始类型和泛型变量的类型

        // ========================================================
        common.intervalLine2();

        // 在调用泛型方法时，可以指定泛型，也可以不指定泛型
        // 在不指定泛型的情况下，泛型变量的类型为该方法中的几种类型的同一父类的最小级，直到Object
        int i = testGeneric.add(1, 2); //这两个参数都是Integer，所以T为Integer类型
        Number f = testGeneric.add(1, 1.2); //这两个参数一个是Integer，一个是Float，故取同一父类的最小级，为Number
        Object o = testGeneric.add(1, "asd"); //这两个参数一个是Integer，一个是Float，故取同一父类的最小级，为Object
        System.out.println(i + " " + f + " " + o);

        // 在指定泛型的情况下，该方法的几种类型必须是该泛型的实例的类型或者其子类
        int e = testGeneric.<Integer>add(1, 2); //指定了Integer，所以只能为Integer类型或者其子类
        // int g = testGeneric.<Integer>add(1, 2.2); //编译错误，指定了Integer，不能为Float
        Number h = testGeneric.<Number>add(1, 2.2); //指定为Number，所以可以为Integer和Float
        System.out.println(e + " " + h);

        // ========================================================
        common.intervalLine2();

        // 其实在泛型类中，不指定泛型的时候，也差不多，只不过这个时候的泛型为Object，就比如ArrayList中，如果不指定泛型，
        // 那么这个ArrayList可以存储任意的对象。
        ArrayList list4 = new ArrayList();
        list4.add(a);
        list4.add(b);
        list4.add(new Date());
        for (int i1 = 0; i1 < list4.size(); i1++) {
            System.out.println(list4.get(i1));
        }

        // ========================================================
        common.intervalLine2();

        /**
         * ArrayList<String> list1 = new ArrayList(); //第一种 情况
         * ArrayList list2 = new ArrayList<String>(); //第二种 情况
         *
         * 因为类型检查就是编译时完成的，new ArrayList()只是在内存中开辟了一个存储空间，可以存储任何类型对象，而真正设计类型
         * 检查的是它的引用，因为我们是使用它引用list1来调用它的方法，比如说调用add方法，所以list1引用能完成泛型类型的检查。
         * 而引用list2没有使用泛型，所以不行。
         */

        // 类型检查就是针对引用的，谁是一个引用，用这个引用调用泛型方法，就会对这个引用调用的方法进行类型检测，
        // 而无关它真正引用的对象。

        // ========================================================
        common.intervalLine2();

        // 类型擦除与多态的冲突和解决办法
        testGeneric3 testGeneric3 = new testGeneric3();
        testGeneric3.setValue(new Date());
        // testGeneric3.setValue(new Object()); // 编译错误


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

    private static void test8(){
        //static proxy
        staticProxy proxy = new staticProxy(new agent());
        proxy.happyMarry();
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

        // c-s -- TCP
//            String ipAdd = "127.0.0.1";
//            int port = 9999;
//            String sendMessage = "D:\\IDEA projects\\practiceCoding\\src\\resources\\photo.jpg";
//
//            ThreadServerTCP threadServer = new ThreadServerTCP(port);
//            new Thread(threadServer).start();
//
//            ThreadClientTCP threadClient = new ThreadClientTCP(ipAdd, port, sendMessage);
//            new Thread(threadClient).start();

        // s-r -- UDP 暂时有问题
            String ipAdd = "localhost";
            int port = 9090;
            String sendMessage = "D:\\IDEA projects\\practiceCoding\\src\\resources\\photo.jpg";

            ThreadReceiveUDP receiveUDP = new ThreadReceiveUDP(port, ipAdd);
            new Thread(receiveUDP);

            ThreadSendUDP sendUDP = new ThreadSendUDP(9082, ipAdd, port);
            new Thread(sendUDP);

//        // Callable
//        testThread5 tt5 = new testThread5(1);
//
//        // 创建执行服务
//        ExecutorService es = Executors.newFixedThreadPool(3);
//
//        // 提交执行
//        Future<Integer> r1 = es.submit(tt5);
//        Future<Integer> r2 = es.submit(tt5);
//        Future<Integer> r3 = es.submit(tt5);
//
//        //获取结果
//        System.out.println(r1.get());
//        System.out.println(r2.get());
//        System.out.println(r3.get());
//
//        //关闭服务
//        es.shutdown();
    }

    private static void test6() throws Exception{
        // ip i = new ip();
        // socket s = new socket();
        String ipAdd = "127.0.0.1";
        int port = 9999;
        String sendMessage = "Hello, world!";

        ClientServerTCP s = new ClientServerTCP(port);

        ClientServerTCP c = new ClientServerTCP(ipAdd, port);
        c.sendMessage(sendMessage);

        String getMessage = s.getMessage();
        System.out.println(getMessage);
    }

    private static void test5(){
        // 单线程测不出来
        long res = 0, num = 999999999999999l;

        for (int j = 0; j < 10; j++) {
            // 算法初启动
            for (int i = 0; i < 10000; i++) {
                common.comparePrime(num);
            }

            res = 0;
            for (int i = 0; i < 10000; i++) {
                res += common.comparePrime(num);
            }
            System.out.println(res);
        }

        // 多线程试试
    }

    private static void test4(){
        singleIntSet set;
        int times = 1;

        common.intervalLine(times++);
        set = new singleIntSet();
        set.Add(3);
        set.Add(7);
        System.out.println(set.Contains(3)); // 输出 true
        System.out.println(set.Contains(5)); // 输出 false

        common.intervalLine(times++);

        set = new singleIntSet2();
        set.Add(13);
        set.Add(17);
        System.out.println(set.Contains(13)); // 输出 true
        System.out.println(set.Contains(15)); // 输出 false

        common.intervalLine(times++);

        set = new singleIntSet3();
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        System.out.println(set.Contains(3));

        common.intervalLine(times++);

        set = new LinkedIntSet4();
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        System.out.println(set.Contains(3));

        common.intervalLine(times++);

        set = new DivisionIntSet();
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        System.out.println(set.Contains(3));

        common.intervalLine(times++);

        set = new DivPrimeIntSet(9);
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        System.out.println(set.Contains(3));

        common.intervalLine(times++);

        set = new MultIntSet(9);
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        System.out.println(set.Contains(3));

        common.intervalLine(times++);

        set = new LineIntSet();
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        set.Add(23);
        set.Remove(13);
        System.out.println(set.Contains(13));
        set.Remove(23);
        System.out.println(set.Contains(23));

        common.intervalLine(times++);

        set = new TwiceIntSet();
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        set.Add(23);
        set.Remove(13);
        System.out.println(set.Contains(23));

        common.intervalLine(times++);

        set = new DoubleIntSet(9);
        set.Add(3);
        System.out.println(set.Contains(3));
        set.Add(13);
        set.Add(23);
        set.Remove(13);
        System.out.println(set.Contains(13));
        System.out.println(set.Contains(23));
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

    private static void test0(){
        int[] array = {22, 34, 3, -32, 82, 55, 89, -50, 37, -5, 64, 35, 9, 70};
        base sort = new bucket(array, false, 2);
        sort.sort();
        sort.show();
    }
}