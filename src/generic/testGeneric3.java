package generic;

import java.util.Date;

public class testGeneric3 extends testGeneric2<Date> {
    @Override
    public void setValue(Date value){
        super.setValue(value);
    }

    @Override
    public Date getValue(){
        return super.getValue();
    }

    /**
     * 本意是重写，实现多态。可是类型擦除后，只能变为了重载。这样，类型擦除就和多态有了冲突。
     * JVM知道你的本意，但是它不能直接实现
     * 于是JVM采用了一个特殊的方法，来完成这项功能，那就是桥方法。
     *
     *   public void setValue(java.util.Date);  //我们重写的setValue方法
     *     Code:
     *        0: aload_0
     *        1: aload_1
     *        2: invokespecial #16                 // Method com/tao/test/Pair.setValue:(Ljava/lang/Object;)V
     *        5: return
     *
     *   public java.util.Date getValue();    //我们重写的getValue方法
     *     Code:
     *        0: aload_0
     *        1: invokespecial #23                 // Method com/tao/test/Pair.getValue:()Ljava/lang/Object;
     *        4: checkcast     #26                 // class java/util/Date
     *        7: areturn
     *
     *   public java.lang.Object getValue();     //编译时由编译器生成的桥方法
     *     Code:
     *        0: aload_0
     *        1: invokevirtual #28                 // Method getValue:()Ljava/util/Date 去调用我们重写的getValue方法;
     *        4: areturn
     *
     *   public void setValue(java.lang.Object);   //编译时由编译器生成的桥方法
     *     Code:
     *        0: aload_0
     *        1: aload_1
     *        2: checkcast     #26                 // class java/util/Date
     *        5: invokevirtual #30                 // Method setValue:(Ljava/util/Date; 去调用我们重写的setValue方法)V
     *        8: return
     *
     * 从编译的结果来看，我们本意重写setValue和getValue方法的子类，竟然有4个方法。
     * 其实不用惊奇，最后的两个方法，就是编译器自己生成的桥方法。
     * 可以看到桥方法的参数类型都是Object，也就是说，子类中真正覆盖父类两个方法的就是这两个我们看不到的桥方法。
     * 而打在我们自己定义的setvalue和getValue方法上面的@Oveerride只不过是假象。
     * 而桥方法的内部实现，就只是去调用我们自己重写的那两个方法。
     * 所以，虚拟机巧妙的使用了桥方法，来解决了类型擦除和多态的冲突。
     *
     * 不过，要提到一点，这里面的setValue和getValue这两个桥方法的意义又有不同。
     * setValue方法是为了解决类型擦除与多态之间的冲突。
     * 而getValue却有普遍的意义，怎么说呢，如果这是一个普通的继承关系
     * 其实这在普通的类继承中也是普遍存在的重写，这就是协变。
     * 协变：test11()
     *
     * 还有一点也许会有疑问，子类中的巧方法Object getValue()和Date getValue()是同时存在的，
     * 可是如果是常规的两个方法，他们的方法签名是一样的，也就是说虚拟机根本不能分别这两个方法。
     * 如果是我们自己编写Java代码，这样的代码是无法通过编译器的检查的，但是虚拟机却是允许这样做的，
     * 因为虚拟机通过参数类型和返回类型来确定一个方法，所以编译器为了实现泛型的多态允许自己做这个
     * 看起来“不合法”的事情，然后交给虚拟器去区别。
     *
     * 也就是说：编译器通过将自己能接受的写法，编译成自己不能接受的写法，而这个写法对于虚拟机而言，
     * 其实是可以接受的，这样，就可以实现本身无法直接支持的功能
     *
     * 再就是说：泛型的Override，从代码上看，从<T>编程<Date>，在编译器眼里，符合的是重载的规范，
     * 但是我本意是为了完成重写，为了完成我的本意，在这里，编译的时候，通过桥方法，
     */
}
