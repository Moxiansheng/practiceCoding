## HotSpot VM

提起HotSpot VM，相信所有Java程序员都知道，它是Sun JDK和OpenJDK中所带的虚拟机，也是目前使用范围最广的Java虚拟机。但不一定所有人都知道的是，这个目前看起来“血统纯正”的虚拟机在最初并非由Sun公司开发，而是由一家名为“Longview Technologies”的小公司设计的；甚至这个虚拟机最初并非是为Java语言而开发的，它来源于Strongtalk VM，而这款虚拟机中相当多的技术又是来源于一款支持Self语言实现“达到C语言50%以上的执行效率”的目标而设计的虚拟机，Sun公司注意到了这款虚拟机在JIT编译上有许多优秀的理念和实际效果，在1997年收购了Longview Technologies公司，从而获得了HotSpot VM。

HotSpot VM既继承了Sun之前两款商用虚拟机的优点（如前面提到的准确式内存管理），也有许多自己新的技术优势，如它名称中的HotSpot指的就是它的热点代码探测技术（其实两个VM基本上是同时期的独立产品，HotSpot还稍早一些，HotSpot一开始就是准确式GC，而Exact VM之中也有与HotSpot几乎一样的热点探测。为了Exact VM和HotSpot VM哪个成为Sun主要支持的VM产品，在Sun公司内部还有过争论，HotSpot打败Exact并不能算技术上的胜利），HotSpot VM的热点代码探测能力可以通过执行计数器找出最具有编译价值的代码，然后通知JIT编译器以方法为单位进行编译。如果一个方法被频繁调用，或方法中有效循环次数很多，将会分别触发标准编译和OSR（栈上替换）编译动作。通过编译器与解释器恰当地协同工作，可以在最优化的程序响应时间与最佳执行性能中取得平衡，而且无须等待本地代码输出才能执行程序，即时编译的时间压力也相对减小，这样有助于引入更多的代码优化技术，输出质量更高的本地代码。

在2006年的JavaOne大会上，Sun公司宣布最终会把Java开源，并在随后的一年，陆续将JDK的各个部分（其中当然也包括了HotSpot VM）在GPL协议下公开了源码，并在此基础上建立了OpenJDK。这样，HotSpot VM便成为了Sun JDK和OpenJDK两个实现极度接近的JDK项目的共同虚拟机。

在2008年和2009年，Oracle公司分别收购了BEA公司和Sun公司，这样Oracle就同时拥有了两款优秀的Java虚拟机：JRockit VM和HotSpot VM。Oracle公司宣布在不久的将来（大约应在发布JDK 8的时候）会完成这两款虚拟机的整合工作，使之优势互补。整合的方式大致上是在HotSpot的基础上，移植JRockit的优秀特性，譬如使用JRockit的垃圾回收器与MissionControl服务，使用HotSpot的JIT编译器与混合的运行时系统。



## 新生代晋升老生代

Hotspot遍历所有对象时，按照年龄从小到大对其所占用的大小进行累积，当累积的某个年龄大小占整个survivor区的比例，超过了survivor区的TargetSurvivorRatio时，取这个年龄和MaxTenuringThreshold中更小的一个值，作为新的晋升年龄阈值

```c
uint ageTable::compute_tenuring_threshold(size_t survivor_capacity) {
    //survivor_capacity是survivor空间的大小
	size_t desired_survivor_size = (size_t)((((double) survivor_capacity)*TargetSurvivorRatio)/100);
  	size_t total = 0;
  	uint age = 1;
  	while (age < table_size) {
    	total += sizes[age];//sizes数组是每个年龄段对象大小
    	if (total > desired_survivor_size) break;
    	age++;
  	}
  	uint result = age < MaxTenuringThreshold ? age : MaxTenuringThreshold;
    ...
}
```

### -XX:MaxTenuringThreshold

该参数主要是控制新生代需要经历多少次GC晋升到老年代中的最大阈值。在JVM中用4个bit存储（放在对象头中）,所以其最大值是15。但并非意味着，对象必须要经历15次YGC才会晋升到老年代中。例如，当survivor区空间不够时，便会提前进入到老年代中，但这个次数一定不大于设置的最大阈值。

### -XX:TargetSurvivorRatio

一个计算期望s区存活大小(Desired survivor size)的参数。默认值为50，即50%。当一个S区中累积的age对象的大小如果大于等于Desired survivor size，则重新计算threshold，以age和MaxTenuringThreshold两者的最小值为准。



## 常量池的分类

### class文件常量池

在Class文件中除了有类的版本【高版本可以加载低版本】、字段、方法、接口等描述信息外，还有一项信息是**常量池(Constant Pool Table)【此时没有加载进内存，也就是在文件中】**，用于存放**编译期生成的各种字面量和符号引用**。

- **字面量**
  字面量类似与我们平常说的常量，主要包括：
  1. 文本字符串：我们在代码中能够看到的字符串，例如String a = “aa”。其中”aa”就是字面量。
  2. 被final修饰的变量。
- **符号引用**
  主要包括以下常量：
  1. 类和接口和全限定名：例如对于String这个类，它的全限定名就是java/lang/String。
  2. 字段的名称和描述符：所谓字段就是类或者接口中声明的变量，包括类级别变量（static)和实例级的变量。
  3. 方法的名称和描述符。所谓描述符就相当于方法的**参数类型+返回值类型**。

### 运行时常量池

类加载器会加载对应的Class文件，而上面的class文件常量池，会在类加载后进入**方法区**中的运行时常量池【此时存在在内存中】。需要的注意的是，运行时常量池是全局共享的，多个类共用一个运行时常量池。并且class文件常量池中多个相同的字符串在运行时常量池只会存在一份（这句话在JDK1.7之前说才对，1.7开始，字符串就不是在运行时常量池中了）。注意运行时常量池存在于**方法区**中（JDK1.8之前方法区被实现为永久代，位于堆中，JDK1.8开始，被移入直接内存中，叫做元数据）。

### 字符串常量池

常量池中的文本字符串会在类加载时进入字符串常量池。JDK1.7之前，字符串常量池位于运行时常量池中，也就是存在于**堆**中的方法区中。到了JDK1.7以及之后的版本中，运行时常量池并没有包含字符串常量池，运行时常量池存在于方法区中（这里的方法区被实现为直接内存中的元数据），而字符串常量池依旧存在于**堆**中。



## StringConstantPool

- **JDK1.7之前运行时常量池逻辑包含字符串常量池存放在方法区, 此时hotspot虚拟机对方法区的实现为永久代**
- **JDK1.7 字符串常量池被从方法区拿到了堆中, 这里没有提到运行时常量池,也就是说字符串常量池被单独拿到堆,运行时常量池剩下的东西还在方法区, 也就是hotspot中的永久代** 。
- **JDK1.8 hotspot移除了永久代用元空间(Metaspace)取而代之, 这时候字符串常量池还在堆, 运行时常量池还在方法区, 只不过方法区的实现从永久代变成了元空间(Metaspace)**



### String.intern()

- JDK1.6及以前先判断字符串常量是否在字符串常量池中，如果存在直接返回该常量，如果没有找到，就在字符串常量区建立该常量；

  ```java
  if(字符串常量 not in 字符串常量池){
      build 字符串常量 in 字符串常量池;
  }
  return 常量池中的常量;
  ```

- JDK1.7及以后

  先判断字符串常量是否在字符串常量池中，如果存在直接返回该常量，如果没有找到，说明该字符串常量在堆中，则把堆区该对象的引用加入到字符串常量池中，以后别人拿到的是该字符串常量的引用，实际存在堆中；

  ```java
  if(字符串常量 in 字符串常量池){
      return 常量池中的常量;
  }else{
      get 字符串常量引用 from 堆区;
      build 字符串常量引用 in 字符串常量池;
  	return 常量池中的引用;
  }
  ```



### String赋值

1. ```java
   String a = "abc";
   // 字符串常量池中创建对象"abc"，堆中没有创建
   
   // 用intern()验证上述结论
   System.out.println(a.intern() == a); // true
   ```

2. ```java
   String a = new String("abc");
   // 字符串常量池中创建对象"abc"，堆中创建指向字符串常量池的对象
      
   // 用intern()验证上述结论
   System.out.println(a.intern() == a); // false
   ```

3. ```java
   String a = "abc";
   String b = new String("abc");
   // a: 字符串常量池中创建对象"abc"，堆中没有创建
   // b: 字符串常量池中没有创建，堆中创建指向字符串常量池的对象
   
   // 用intern()验证上述结论
   System.out.println(a == b); // false
   // a指向的字符串常量池，b指向的堆，故false
   System.out.println(b.intern() == a); // true
   // 字符串常量池中已存在"abc"，即a，故b.intern()返回a
   System.out.println(a.intern() == b); // false  
   // 字符串常量池中已存在"abc"，即a，故a.intern()返回a
   // 上式a.intern() == b等价为a == b
   ```

4. ```java
   String a = "ab" + new String("cd");
   // 当出现非字面量字符串参与拼接时（加号两侧反过来也一样）
   // 会在底层使用StringBuider的append()方法进行拼接
   // 最后再用toString()方法赋值
      
   // 1."ab"会按1中那样，在字符串常量池中创建"ab"对象
   // 2.将"ab"初始化入StringBuilder中
   // 3.new String("cd")会按2中那样，在字符串常量池和堆中创建对象
   // 4.将"cd"用append()方法添入StringBuilder中
   // 5.StringBuilder调用toString()方法在堆中创建"abcd"String对象
   // 6.将这个String对象引用赋予a
      
   // 总结：字符串常量池中创建了"ab","cd"两个对象；
   // 堆中创建了new String("cd")这个匿名对象和
   // "abcd"String对象，共两个对象。
      
   // 用intern()验证"abcd"在堆中却不在字符串常量池中
   System.out.println(a.intern() == a); // <JDK1.7: false; >=JDK1.7: true
   // 两种JDK下结果不同，说明事实确实如此
      
   // 用intern()验证"ab","cd"在字符串常量池中
   String b = "a" + new String("b");
   // 根据上一个验证，此时的b没有在字符串常量池中创建"ab"
   System.out.println(b.intern() == b); // false
   // false说明，字符串常量池中有"ab"，而这又不是b创建的，
   // 因此说明"ab"是由a创建在字符串常量池中的
   ```

5. ```java
   String a = new String("ab") + new String("cd");
   // 同样是非字面量字符串参与拼接时，
   // 会在底层使用StringBuider的append()方法进行拼接
   // 最后再用toString()方法赋值
   
   // 1.new String("ab")会按2中那样，在字符串常量池和堆中创建对象，
   // 2.将"ab"初始化入StringBuilder中
   // 3.new String("cd")会按2中那样，在字符串常量池和堆中创建对象
   // 4.将"cd"用append()方法添入StringBuilder中
   // 5.StringBuilder调用toString()方法在堆中创建"abcd"String对象
   // 6.将这个String对象引用赋予a
   
   // 总结：字符串常量池中创建了"ab","cd"两个对象；
   // 堆中创建了new String("ab")与new String("cd")两个匿名对象和
   // "abcd"String对象，共三个对象。
   
   // intern()验证与4相同
   ```

6. ```java
   String a = "ab" + "cd";
   // JVM进行字符串拼接优化，在编译阶段就把"ab"和"cd"拼接为了"abcd"
   // 等同于了 String a = "abcd";
   // 因此也就只有一个对象"abcd"在字符串常量池中创建
      
   // intern()验证只有一个对象"abcd"在字符串常量池中创建
   System.out.println(a.intern() == a); // true
   ```

7. ```java
   String a = new String("ab" + "cd");
   // JVM进行字符串拼接优化，在编译阶段就把"ab"和"cd"拼接为了"abcd"
   // 等同于了 String a = new String("abcd");
   // 此时就转化成了2
   
   // 用intern()验证上述结论
   String b = new String("a") + new String("b");
   System.out.println(b.intern() == b); // true
   // 如4中所证，b不会在字符串常量池中创建对象"ab"，
   // 所以结果为true，说明a也没有在字符串常量池中创建对象"ab"
   ```

8. ```java
   String a = new String("ab" + new String("cd"));
   // 1.先看"ab" + new String("cd"),按4分析，"abcd"不在字符串常量池中
   // 2.new String()会在字符串常量池中创建"abcd"，也会在堆中创建对象，如2分析
   
   // 用intern()验证上述结论
   String b = new String(a);
   System.out.println(b.intern() == b); // false
   // false说明b.intern()返回的与b不是同一个对象，
   // 而由4又可知不会将"abcd"创建在字符串常量池中，
   // 所以此时在字符串常量池中的"abcd"对象是由b创建的，
   // 这里相当于取了a的字符串值，将其传入new String()中，
   // 也就好比于2的分析
   ```



## 安全点与安全区域与记忆集

- 枚举GC Root（可达性分析的第一步）

  - 因：每次枚举如果都要遍历栈，时间成本太大
  - 果：使用OopMap数据结构，**记录栈上本地变量到堆上对象的引用关系**
  - 因：Oop需要更新，但执行一条指令就改一次，成本太高
  - 果：采用在**安全点**更新的方法，安全点需要将程序运行时间划分合理，一般在
    - **方法调用、循环跳转、异常跳转**这些长时间执行的指令复用行为中设置**安全点**
  - 因：为保证枚举根节点的准确性
  - 果：需要Stop the world(GC停顿)，冻结整个应用

- #### **如何让所有线程跑到最近的安全点再停顿下来以便进行GC？**

  - 抢先式中断：
    - 先中断所有线程
    - 发现有线程没中断在安全点，恢复它让它跑到安全点
  - 主动式中断（主要使用）：
    - 设置一个中断标记
    - 每个线程到达安全点时，检查此标记，选择是否中断自己

- #### **安全区域**

  - 指在一段代码片段中，引用关系不会发生变化，在这个区域中任意位置开始GC都是安全的
  - 因：一个Sleep或Blocked状态的线程无法自己到达安全点并中断自己，GC不能一直等待它
  - 果：线程执行到安全区域时，检查系统是否在GC，若不在GC，则继续执行；若在GC，等GC结束再继续执行

- #### 记忆集

  - 解决跨代引用，使用记忆集可以缩减GC Root扫描范围



## 闭包

```javascript
　　function f1(){
　　　　var n=999;

　　　　function f2(){
　　　　　　alert(n);
　　　　}

　　　　return f2;

　　}

　　var result=f1();

　　result(); // 999
```

- f2函数就是闭包。为了获取f1内部变量，因此返回闭包函数f2。
- 这能成立的原因是，子函数可以获取父函数的局部变量。
- 闭包就是能够读取其他函数内部变量的函数。可以理解为”定义在一个函数内部的函数”。
- 在本质上，闭包就是将函数内部和函数外部连接起来的一座桥梁。



## 类的被动引用（不会发生类的初始化）

- 当访问一个静态域时，只有真正声明这个域的类才会被初始化。如：当通过子类引用父类的静态变量（static），不会导致子类的初始化
- 通过数据定义类引用，不会触发此类的初始化。
- 引用常量（final）不会触发此类的初始化（常量在连接阶段就存入类的常量池中了）



## 什么情况下发生GC(when)

- #### Minor GC触发的条件
  - 新生代中Eden空间不足,对象优先在Eden中分配，当Eden中没有足够空间时，虚拟机将发生一次Minor GC，因为Java大多数对象都是朝生夕灭，所以Minor GC非常频繁，而且速度也很快；
  - 发生Minor GC时，虚拟机会检测之前每次晋升到老年代的平均大小是否大于老年代的剩余空间大小，如果大于，则进行一次Full GC，如果小于，则查看HandlePromotionFailure设置是否允许担保失败，如果允许，那只会进行一次Minor GC，如果不允许，则改为进行一次Full GC。

- #### Full GC触发的条件
  - System.gc()方法的调用，此方法是建议JVM进行Full GC,虽然只是建议而非一定,但很多情况下它会触发 Full GC,从而增加Full GC的频率,也即增加了间歇性停顿的次数。一般情况下不使用此方法，让虚拟机自己去管理它的内存，可通过-XX:+ DisableExplicitGC来禁止RMI调用System.gc()。 
  - 老年代的内存空间不足，发生Full GC一般都会有一次Minor GC。大对象直接进入老年代，如很长的字符串数组，虚拟机提供一个-XX:PretenureSizeThreadhold参数，令大于这个参数值的对象直接在老年代中分配，避免在Eden区和两个Survivor区发生大量的内存拷贝；
  - 方法区内存空间不足，Permanet Generation中存放的为一些class的信息、常量、静态变量等数据，当系统中要加载的类、反射的类和调用的方法较多时，Permanet Generation可能会被占满，在未配置为采用CMS GC的情况下也会执行Full GC。如果经过Full GC仍然回收不了，那么JVM会抛出如下错误信息：java.lang.OutOfMemoryError: PermGen space为避免Perm Gen占满造成Full GC现象，可采用的方法为增大Perm Gen空间或转为使用CMS GC。
  - 当Minor GC时，老年代的剩余空间小于历次从新生代往老年代中移的对象的平均内存空间大小时，Hotspot为了避免由于新生代对象晋升到旧生代导致旧生代空间不足的现象，在进行Minor GC时，做了一个判断，如果之前统计所得到的Minor GC晋升到旧生代的平均大小大于旧生代的剩余空间，那么就直接触发Full GC。
    - 例如程序第一次触发Minor GC后，有6MB的对象晋升到旧生代，那么当下一次Minor GC发生时，首先检查旧生代的剩余空间是否大于6MB，如果小于6MB，则执行Full GC。
    - 当新生代采用PS GC时，方式稍有不同，PS GC是在Minor GC后也会检查，例如上面的例子中第一次Minor GC后，PS GC会检查此时旧生代的剩余空间是否大于6MB，如小于，则触发对旧生代的回收。
    - 对于使用RMI来进行RPC或管理的Sun JDK应用而言，默认情况下会一小时执行一次Full GC。可在启动时通过-java -Dsun.rmi.dgc.client.gcInterval=3600000来设置Full GC执行的间隔时间或通过-XX:+ DisableExplicitGC来禁止RMI调用System.gc。
  - 堆中分配很大的对象，大对象是指需要大量连续内存空间的java对象，例如很长的数组，此种对象会直接进入老年代，而老年代虽然有很大的剩余空间，但是无法找到足够大的连续空间来分配给当前对象，此种情况就会触发JVM进行Full GC。为了解决这个问题，CMS垃圾收集器提供了一个可配置的参数，即-XX:+UseCMSCompactAtFullCollection开关参数，用于在“享受”完Full GC服务之后额外免费赠送一个碎片整理的过程，内存整理的过程无法并发的，空间碎片问题没有了，但提顿时间不得不变长了，JVM设计者们还提供了另外一个参数 -XX:CMSFullGCsBeforeCompaction,这个参数用于设置在执行多少次不压缩的Full GC后,跟着来一次带压缩的。



## happens-before

**一、程序顺序原则：** 即在一个线程内必须保证语义串行性，也就是说按照代码顺序执行。

**二、锁规则：** 解锁(unlock)操作必然发生在后续的同一个锁的加锁(lock)之前，也就是说，如果对于一个锁解锁后，再加锁，那么加锁的动作必须在解锁动作之后(同一个锁)。

**三、volatile规则：**   volatile变量的写，先发生于读，这保证了volatile变量的可见性，简单的理解就是，volatile变量在每次被线程访问时，都强迫从主内存中读该变量的值，而当该变量发生变化时，又会强迫将最新的值刷新到主内存，任何时刻，不同的线程总是能够看到该变量的最新值。

**四、线程启动规则：**   线程的start()方法先于它的每一个动作，即如果线程A在执行线程B的start方法之前修改了共享变量的值，那么当线程B执行start方法时，线程A对共享变量的修改对线程B可见。

**五、传递性优先级规则：**  A先于B ，B先于C 那么A必然先于C。

**六、线程终止规则：** 线程的所有操作先于线程的终结，Thread.join()方法的作用是等待当前执行的线程终止。假设在线程B终止之前，修改了共享变量，线程A从线程B的join方法成功返回后，线程B对共享变量的修改将对线程A可见。

**七、线程中断规则：** 对线程 interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生，可以通过Thread.interrupted()方法检测线程是否中断。

**八、对象终结规则：** 对象的构造函数执行，结束先于finalize()方法。



## 对象的内存布局

- ### 对象头（64位机器）

  > **Mark Word 64bits**
  >
  > > | 锁状态/gc | -                             | -           | -        | -     | 偏向锁标志位1bit | 锁标志位2bit |
  > > | --------- | ----------------------------- | ----------- | -------- | ----- | ---------------- | ------------ |
  > > | 无锁      | unused:25                     | hashcode:31 | unused:1 | age:4 | 0                | 01           |
  > > | 偏向锁    | thread:54                     | epoch:2     | unused:1 | age:4 | 1                | 01           |
  > > | 轻量级锁  | ptr_to_lock_record:62         | -           | -        | -     | -                | 00           |
  > > | 重量级锁  | ptr_to_heavyweight_monitor:62 | -           | -        | -     | -                | 10           |
  > > | gc标记    | -                             | -           | -        | -     | -                | 11           |
  > >
  > > **age**：4位的Java对象年龄。在GC中，如果对象在Survivor区复制一次，年龄增加1。当对象达到设定的阈值时，将会晋升到老年代。默认情况下，并行GC的年龄阈值为15，并发GC的年龄阈值为6。由于age只有4位，所以最大值为15，这就是`-XX:MaxTenuringThreshold`选项最大值为15的原因。
  > >
  > > **hashcode**：31位的对象标识Hash码，采用延迟加载技术。调用方法`System.identityHashCode()`计算，并会将结果写到该对象头中。当对象被锁定时，该值会移动到管程Monitor中。
  > >
  > > **thread**：持有偏向锁的线程ID
  > >
  > > **epoch**：偏向时间戳
  > >
  > > **ptr_to_lock_record**：指向栈中所记录的指针
  > >
  > > **ptr_to_heavyweight_monitor**：指向管程Monitor的指针
  > >
  > > *只有在锁标志位为01的时候，才会去看偏向锁标志位是0还是1*
  >
  > 
  >
  > **Klass pointer 64bits-uncompressed/32bits-compressed(default)**
  >
  > > 这一部分用于存储对象的类型指针，该指针指向它的类元数据，JVM通过这个指针确定对象是哪个类的实例。该指针的位长度为JVM的一个字大小，即32位的JVM为32位，64位的JVM为64位。
  > > 如果应用的对象过多，使用64位的指针将浪费大量内存，统计而言，64位的JVM将会比32位的JVM多耗费50%的内存。为了节约内存可以使用选项`+UseCompressedOops`开启指针压缩，其中，oop即ordinary object pointer普通对象指针。开启该选项后，下列指针将压缩至32位：
  > >
  > > 1. 每个Class的属性指针（即静态变量）
  > > 2. 每个对象的属性指针（即对象变量）
  > > 3. 普通对象数组的每个元素指针
  > >
  > > 当然，也不是所有的指针都会压缩，一些特殊类型的指针JVM不会优化，比如指向PermGen的Class对象指针(JDK8中指向元空间的Class对象指针)、本地变量、堆栈元素、入参、返回值和NULL指针等。
  >
  > 
  >
  > **array length** 
  >
  > > 如果对象是一个数组，那对象头还需有额外的空间用于存储数组的长度，这部分数据的长度也随着JVM架构的不同而不同：32位的JVM上，长度为32位；64位JVM则为64位。64位JVM如果开启`+UseCompressedOops`选项，**该区域长度也将由64位压缩至32位**。

- ### 实例数据

  **实例数据部分是对象真正存储的有效信息**，也是在程序中所定义的各种类型的字段内容。

- ### 对齐填充

  **对齐填充部分不是必然存在的，也没有什么特别的含义，仅仅起占位作用。** 因为 Hotspot 虚拟机的自动内存管理系统要求对象起始地址**必须是 8 字节的整数倍**，换句话说就是对象的大小必须是 8 字节的整数倍。而对象头部分正好是 8 字节的倍数（1 倍或 2 倍），因此，当对象实例数据部分没有对齐时，就需要通过对齐填充来补全。



## JVM锁升级

### 轻量级锁CAS操作之前堆栈与对象的状态

![Before](D:\IDEA projects\practiceCoding\docs\java\LightLockBeforeCAS.jpg)

### 轻量级锁CAS操作之后堆栈与对象的状态

![After](D:\IDEA projects\practiceCoding\docs\java\LightLockAfterCAS.jpg)

### Lock Record简介

#### 用途

用于偏向锁优化和轻量级锁优化

#### 数据结构

在openjdk中通过两个类BasicObjectLock和BasicLock来实现：

```c++
class BasicLock VALUE_OBJ_CLASS_SPEC {
 private:
  volatile markOop _displaced_header;
};

// A BasicObjectLock associates a specific Java object with a BasicLock.
// It is currently embedded in an interpreter frame.
class BasicObjectLock VALUE_OBJ_CLASS_SPEC {
 private:
  BasicLock _lock;   // the lock, must be double word aligned; markword
  oop       _obj;    // object holds the lock; pointer
};
```

#### 创建时机

当字节码解释器执行monitorenter字节码轻度锁住一个对象时，就会在获取锁的线程的栈上显式或者隐式分配一个lock record

#### 创建位置

lock record在线程的Interpretered Frame上(解释帧)分配

#### 作用

- 持有displaced word和锁住对象的元数据
- 解释器使用lock record来检测非法的锁状态
- 隐式地充当锁重入机制的计数器

### 完整锁升级流程图

![JVMLockUpdate](D:\IDEA projects\practiceCoding\docs\java\JVMLockUpdate.jpg)

### 结合图与源码对完整JVM锁升级流程进行总结

- 前置知识：

  - JDK1.6默认开启偏向锁`-XX：+UseBiasedLocking`，初始为未锁定、未偏向且可偏向对象（101），此时第一次进程来获取对象锁，会进入到偏向锁阶段

  - （101）下有三种状态：

    - **匿名偏向（Anonymously biased)**

      若**threadID处为null（0）**，意味着还没有线程偏向于这个锁对象。第一个试图获取该锁的线程将会面临这个情况，使用原子CAS指令可将该锁对象绑定于当前线程。这是允许偏向锁的类对象的初始状态。

    - **可重偏向（Rebiasable）**

      在此状态下，偏向锁的epoch字段是无效的。下一个试图获取锁对象的线程将会面临这个情况，使用原子CAS指令可将该锁对象绑定于当前线程。在批量重偏向的操作中，未被持有的锁对象都被置于这个状态，以便允许被快速重偏向。

    - **已偏向（Biased）**

      这种状态下，thread ptr非空，且epoch为有效值——意味着其他线程正在只有这个锁对象。

    > epoch是否有效：与锁对象对应的klass的mark_prototype的epoch值是否匹配。匹配则有效，不匹配则无效。

  - 若使用`-XX：-UseBiasedLocking`显式关闭，则初始为未锁定、不可偏向对象（001），此时第一次进程来获取对象锁，会进入到轻量级锁阶段

  - HotSpot为所有加载的类型，在class元数据——InstanceKlass中保留了一个MarkWord原型——mark_prototype。这个值的bias位域决定了该类型的对象是否允许被偏向锁定。与此同时，当前的epoch位也被保留在prototype中。这意味着，对应class的新对象可以简单地直接拷贝这个原型值，而不必在后面进行修正。在批量重偏向(bulk rebias)的操作中，prototype的epoch位将会被更新；在批量吊销(bulk revoke)的操作中，prototype将会被置成不可偏向的状态——bias位被置0。

  - 如果是在JVM启动后的头四秒内，即使偏向锁的特性被打开，出于性能（启动时间）的原因，JVM也会将其禁止，即在这四秒内，prototype MarkWord的bias位被设置为0，因为新对象是copy的这个原型值，所以在这四秒内创建的对象，禁止被偏向，因为它们的bias位copy的为0。四秒之后，prototype MarkWord的bias位会被重设为1，这时开始，再创建的新对象就可以被偏向了。`-XX:BiasedLockingStartupDelay=0`可以关闭这四秒的延迟，当然也可以设置为其他值。+

  - 

- 具体流程

  - Monitor：openjdk\hotspot\src\share\vm\runtime\objectMonitor.hpp
  - MarkWord：openjdk\hotspot\src\share\vm\oops\markOop.hpp
  - monitorenter|exit指令：openjdk\hotspot\src\share\vm\interpreter\interpreterRuntime.cpp
  - 偏向锁：openjdk\hotspot\src\share\vm\runtime\biasedLocking.cpp

> **验证锁标志位**
>
> 若为01，进入下一步验证；若为00，进入轻量级锁阶段；若为10，进入重量级锁阶段
>
> **验证对象的bias位**
>
> 若为1，进入偏向锁阶段；若为0，进入轻量级锁阶段
>
> *对类的prototype中的可偏向标志位置为0有两点作用：*
>
> - 该类新分配的对象都是**无锁**状态。
> - 后续，线程请求该类的偏向锁对象时，**发现类的可偏向属性被禁用，则直接加轻量级锁**。（针对的是批量撤销时，未被线程正在持有的偏向锁。）
>
> **偏向锁阶段**
>
> > **偏向锁获取 bind bias**
> >
> > > 1. **验证epoch位是否有效以及ThreadID处是否为空**
> > >
> > >    |                  | epoch有效       | epoch无效           |
> > >    | ---------------- | --------------- | ------------------- |
> > >    | thread_ptr为null | 匿名偏向：步骤3 | 匿名可重偏向：步骤3 |
> > >    | thread_ptr非null | 已偏向：步骤2   | 可重偏向：步骤2     |
> > >
> > > 2. 将当前线程ID与Mark Word中的线程ID进行匹配，若相同，则为锁重入：此线程已获取到该对象的偏向锁，无需进行其它锁操作，便可直接执行同步锁的代码。重入的时候，仍然会隐式的生成lock record，其中`_lock`存的为null，`_obj`指向对象的Mark Word（这里体现了lock record的作用之三，隐式地充当锁重入机制的计数器）。若不同，则进入revoke&rebias阶段
> > >
> > > 3. CAS操作，将当前线程ID放入对象Mark Word的线程ID区域，获取到偏向锁，接下来可以执行同步锁代码。获取到偏向锁时，会隐式的创建一个lock record，`_lock`存的是自己的线程ID，`_obj`指向对象的Mark Word。
> > >
> > > *请求锁时lock record操作的源代码*
> > >
> > > > fast_enter: 可偏向-偏向锁启动
> > > >
> > > > slow_enter: 不可偏向-偏向锁关闭
> > >
> > > ```c++
> > > CASE(_monitorenter): {
> > >   // lockee 就是锁对象
> > >   oop lockee = STACK_OBJECT(-1);
> > >   // derefing's lockee ought to provoke implicit null check
> > >   CHECK_NULL(lockee);
> > >   // ***************************
> > >   // 后面的循环是按照从低地址到高地址的顺序，试图找在已关联该锁的锁记录之前，
> > >   // 最后一个空闲的锁记录（没有指向任何锁对象）。
> > >   // 那也就是意味着，新找到的空闲lock record的地址
> > >   // 小于找到的关联lock record的地址
> > >   //
> > >   // 如果找不到关联的lock record，就找最后一个空闲lock record
> > >   // 找不到意味着这次是第一次关联，
> > >   // 而找最后一个空闲，意味着最早的第一次关联的lock record地址最大
> > >   // ***************************
> > >   BasicObjectLock* limit = istate->monitor_base();
> > >   BasicObjectLock* most_recent = (BasicObjectLock*) istate->stack_base();
> > >   BasicObjectLock* entry = NULL;
> > >   while (most_recent != limit ) {
> > >     if (most_recent->obj() == NULL) entry = most_recent;
> > >     // 碰到已存在关联该锁的 lock record，直接退出
> > >     else if (most_recent->obj() == lockee) break;
> > >     most_recent++;
> > >   }
> > >   // entry 不为 NULL，代表还有空闲的 lock record
> > >   if (entry != NULL) {
> > >   ...
> > >   } else {
> > >     // lock record 不够，重新执行
> > >     istate->set_msg(more_monitors);
> > >     UPDATE_PC_AND_RETURN(0); // Re-execute
> > >   }
> > > }
> > > ```
> > >
> > > 
> >
> > **（批量）撤销与重偏向 revoke&rebias**
> >
> > > 单个的撤销操作
> > >
> > > 源码 [BiasedLocking.cpp](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/biasedLocking.cpp#l146)
> > >
> > > ```c++
> > > static BiasedLocking::Condition revoke_bias(oop obj, bool allow_rebias, bool is_bulk, JavaThread* requesting_thread) {
> > >   markOop mark = obj->mark();
> > >   // 如果对象不是偏向锁，直接返回 NOT_BIASED
> > >   if (!mark->has_bias_pattern()) {
> > >     ...
> > >     return BiasedLocking::NOT_BIASED;
> > >   }
> > > 
> > >   uint age = mark->age();
> > >   // 构建两个 mark word，一个是匿名偏向模式（101），一个是无锁模式（001）
> > >   markOop   biased_prototype = markOopDesc::biased_locking_prototype()->set_age(age);
> > >   markOop unbiased_prototype = markOopDesc::prototype()->set_age(age);
> > > 
> > >   ...
> > > 
> > >   JavaThread* biased_thread = mark->biased_locker();
> > >   if (biased_thread == NULL) {
> > > 	// Object is anonymously biased. We can get here if, for
> > > 	// example, we revoke the bias due to an identity hash code
> > > 	// being computed for an object.
> > >     // 对象是匿名的。例如，如果我们撤销由于为对象计算身份哈希代码而产生的偏向，
> > >     // 我们就可以到达这里。
> > >     
> > > 	// 如果不允许重偏向，则将对象的 mark word 设置为无锁模式
> > >     if (!allow_rebias) {
> > >       obj->set_mark(unbiased_prototype);
> > >     }
> > >     ...
> > >     return BiasedLocking::BIAS_REVOKED;
> > >   }
> > > 
> > >   // 判断偏向线程是否还存活
> > >   bool thread_is_alive = false;
> > >   // 如果当前线程就是偏向线程 
> > >   if (requesting_thread == biased_thread) {
> > >     thread_is_alive = true;
> > >   } else {
> > >      // 遍历当前 jvm 的所有线程，如果能找到，则说明偏向的线程还存活
> > >     for (JavaThread* cur_thread = Threads::first(); cur_thread != NULL; cur_thread = cur_thread->next()) {
> > >       if (cur_thread == biased_thread) {
> > >         thread_is_alive = true;
> > >         break;
> > >       }
> > >     }
> > >   }
> > >   // 如果偏向的线程已经不存活了
> > >   if (!thread_is_alive) {
> > >     // 如果允许重偏向，则将对象 mark word 设置为匿名偏向状态，否则设置为无锁状态
> > >     if (allow_rebias) {
> > >       obj->set_mark(biased_prototype);
> > >     } else {
> > >       obj->set_mark(unbiased_prototype);
> > >     }
> > >     ...
> > >     return BiasedLocking::BIAS_REVOKED;
> > >   }
> > >     
> > >   // 线程还存活则遍历线程栈中所有的 lock record
> > >   // Thread owning bias is alive.
> > >   // Check to see whether it currently owns the lock and, if so,
> > >   // write down the needed displaced headers to the thread's stack.
> > >   // Otherwise, restore the object's header either to the unlocked
> > >   // or unbiased state.
> > >   // 拥有偏向锁的线程还活跃着。
> > >   // 检查它当前是否拥有锁
> > >   // 是否拥有锁是判断最早的关联lock record是否存在
> > >     
> > >   // 如果是，则把需要的_lock写入线程栈中。
> > >   // 即：如果是拥有着锁，那就需要改造所有的lock record
> > >   // 
> > >   // the needed：
> > >   // 指的是最早的lock record和之后重用产生的lock record
> > >   // 在_lock部分有所不同，按需填写，_obj部分是相同的
> > >   // 改造的流程：
> > >   // 1.将所有的lock record（包括最早的）都设置为
> > >   //   一个空_lock和_obj指向对象头地址的lock record
> > >   //   轻量锁下重用lock record的格式
> > >   // 2.再单独重置最早的lock record
> > >   //   因为流程1内已经对_obj设置完毕，所以highest_lock
> > >   //   只用重置一个为无锁状态对象的Mark Word的_lock
> > >   //   然后让对象的Mark Word指向这个最老的版本
> > >   
> > >   // 否则，将对象的头还原为未锁定或无偏差状态。  
> > >   // 即：如果未拥有着锁，就根据是否可重偏向来决定
> > >     
> > >   GrowableArray<MonitorInfo*>* cached_monitor_info = get_or_compute_monitor_info(biased_thread);
> > >   BasicLock* highest_lock = NULL;
> > >   for (int i = 0; i < cached_monitor_info->length(); i++) {
> > >     MonitorInfo* mon_info = cached_monitor_info->at(i);
> > >     // 如果能找到对应的 lock record，说明偏向所有者正在持有锁
> > >     if (mon_info->owner() == obj) {
> > >       ...
> > >       // 升级为轻量级锁，修改栈中所有关联该锁的 lock record
> > >       // 先处理所有锁重入的情况，
> > >       // 轻量级锁的 displaced mark word 为 NULL，表示锁重入
> > >       // 这里会把最早的lock record也重置了，后续会单独处理
> > >       markOop mark = markOopDesc::encode((BasicLock*) NULL);
> > >       highest_lock = mon_info->lock();
> > >       highest_lock->set_displaced_header(mark);
> > >     } else {
> > >       ...
> > >     }
> > >   }
> > >   if (highest_lock != NULL) { // highest_lock 如果非空，则它是最早关联该锁的 lock record
> > >     // 这个 lock record 是线程彻底退出该锁的最后一个 lock record
> > >     // 所以要，设置 lock record 的 displaced mark word 为无锁状态的 mark word
> > >     // 并让锁对象的 mark word 指向当前 lock record
> > >     highest_lock->set_displaced_header(unbiased_prototype);
> > >     obj->release_set_mark(markOopDesc::encode(highest_lock));
> > >     ...
> > >   } else {
> > >     // 走到这里说明偏向所有者没有正在持有锁
> > >     ...
> > >     if (allow_rebias) {
> > >        // 设置为匿名偏向状态
> > >       obj->set_mark(biased_prototype);
> > >     } else {
> > >       // 将 mark word 设置为无锁状态
> > >       obj->set_mark(unbiased_prototype);
> > >     }
> > >   }
> > > 
> > >   return BiasedLocking::BIAS_REVOKED;
> > > }
> > > 
> > > ```
> > >
> > > 1. 判断原偏向锁线程是否存活。判断方法如源码所示，对jvm所有线程进行遍历，检测其中是否还有Mark Word中记录的线程。如果线程存活，进入步骤2做进一步判断；如果线程死亡，一定未持有偏向锁，进入步骤3
> > > 2. 线程存活，可能持有偏向锁：遍历存活的原偏向锁线程的线程栈中所有的lock record（遍历操作需要在**安全点**执行，此时原偏向锁进程将被阻塞暂停），查看能否找到关联该锁的lock record。若能找到，则原偏向锁线程仍持有对象，进入步骤4；若找不到，则原偏向锁线程未持有偏向锁，进入步骤3
> > > 3. 未持有偏向锁：根据是否允许重偏向对Mark Word进行后续设置。若允许重定向，则将其重置为（101-[thread_ptr:highest_lock, epoch:无效]）状态，新进程重新进入偏向锁获取阶段；若不允许，则重置为（001）状态，新进程进入到轻量级锁获取阶段。
> > > 4. 持有偏向锁：修改在原偏向锁进程中所有与该锁关联的锁记录（修改的详细内容在源码注释中），并将该对象升级为轻量级锁（00-[thread_ptr:origin thread]）状态。修改过后，将原来的原偏向锁线程与偏向锁对象，改造成了轻量级锁线程和轻量级锁对象。锁的所有权未变，但级别晋升了。而当前线程则进入轻量级锁获取阶段。
> > >
> > > 批量撤销与批量重偏向
> > >
> > > 源码 [biasedLocking.cpp](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/biasedLocking.cpp#l321)
> > >
> > > ```c++
> > > static BiasedLocking::Condition bulk_revoke_or_rebias_at_safepoint(oop o,
> > >                                    bool bulk_rebias,
> > >                                    bool attempt_rebias_of_object,
> > >                                    JavaThread* requesting_thread) {
> > >   // 安全点检测，必须在安全点才能进行批量撤销或者重偏向
> > >     assert(SafepointSynchronize::is_at_safepoint(), "must be done at safepoint");
> > > 
> > >   ...
> > > 
> > >   jlong cur_time = os::javaTimeMillis();
> > >   o->klass()->set_last_biased_lock_bulk_revocation_time(cur_time);
> > > 
> > >   Klass* k_o = o->klass();
> > >   Klass* klass = k_o;
> > > 
> > >   if (bulk_rebias) {
> > >     // Use the epoch in the klass of the object to implicitly revoke
> > >     // all biases of objects of this data type and force them to be
> > >     // reacquired. However, we also need to walk the stacks of all
> > >     // threads and update the headers of lightweight locked objects
> > >     // with biases to have the current epoch.
> > > 
> > >     // If the prototype header doesn't have the bias pattern, don't
> > >     // try to update the epoch -- assume another VM operation came in
> > >     // and reset the header to the unbiased state, which will
> > >     // implicitly cause all existing biases to be revoked
> > >     if (klass->prototype_header()->has_bias_pattern()) {
> > >       int prev_epoch = klass->prototype_header()->bias_epoch();
> > >       klass->set_prototype_header(klass->prototype_header()->incr_bias_epoch());
> > >       int cur_epoch = klass->prototype_header()->bias_epoch();
> > > 
> > >       // Now walk all threads' stacks and adjust epochs of any biased
> > >       // and locked objects of this data type we encounter
> > >       for (JavaThread* thr = Threads::first(); thr != NULL; thr = thr->next()) {
> > >         GrowableArray<MonitorInfo*>* cached_monitor_info = get_or_compute_monitor_info(thr);
> > >         for (int i = 0; i < cached_monitor_info->length(); i++) {
> > >           MonitorInfo* mon_info = cached_monitor_info->at(i);
> > >           oop owner = mon_info->owner();
> > >           markOop mark = owner->mark();
> > >           if ((owner->klass() == k_o) && mark->has_bias_pattern()) {
> > >             // We might have encountered this object already in the case of recursive locking
> > >             assert(mark->bias_epoch() == prev_epoch || mark->bias_epoch() == cur_epoch, "error in bias epoch adjustment");
> > >             owner->set_mark(mark->set_bias_epoch(cur_epoch));
> > >           }
> > >         }
> > >       }
> > >     }
> > > 
> > >     // At this point we're done. All we have to do is potentially
> > >     // adjust the header of the given object to revoke its bias.
> > >     revoke_bias(o, attempt_rebias_of_object && klass->prototype_header()->has_bias_pattern(), true, requesting_thread);
> > >   } else {
> > >     ...
> > > 
> > >     // Disable biased locking for this data type. Not only will this
> > >     // cause future instances to not be biased, but existing biased
> > >     // instances will notice that this implicitly caused their biases
> > >     // to be revoked.
> > >     klass->set_prototype_header(markOopDesc::prototype());
> > > 
> > >     // Now walk all threads' stacks and forcibly revoke the biases of
> > >     // any locked and biased objects of this data type we encounter.
> > >     for (JavaThread* thr = Threads::first(); thr != NULL; thr = thr->next()) {
> > >       GrowableArray<MonitorInfo*>* cached_monitor_info = get_or_compute_monitor_info(thr);
> > >       for (int i = 0; i < cached_monitor_info->length(); i++) {
> > >         MonitorInfo* mon_info = cached_monitor_info->at(i);
> > >         oop owner = mon_info->owner();
> > >         markOop mark = owner->mark();
> > >         if ((owner->klass() == k_o) && mark->has_bias_pattern()) {
> > >           revoke_bias(owner, false, true, requesting_thread);
> > >         }
> > >       }
> > >     }
> > > 
> > >     // Must force the bias of the passed object to be forcibly revoked
> > >     // as well to ensure guarantees to callers
> > >     revoke_bias(o, false, true, requesting_thread);
> > >   }
> > > 
> > >   ...
> > > 
> > >   BiasedLocking::Condition status_code = BiasedLocking::BIAS_REVOKED;
> > > 
> > >   if (attempt_rebias_of_object &&
> > >       o->mark()->has_bias_pattern() &&
> > >       klass->prototype_header()->has_bias_pattern()) {
> > >     markOop new_mark = markOopDesc::encode(requesting_thread, o->mark()->age(),
> > >                                            klass->prototype_header()->bias_epoch());
> > >     o->set_mark(new_mark);
> > >     status_code = BiasedLocking::BIAS_REVOKED_AND_REBIASED;
> > > 	...
> > >   }
> > > 
> > >   assert(!o->mark()->has_bias_pattern() ||
> > >          (attempt_rebias_of_object && (o->mark()->biased_locker() == requesting_thread)),
> > >          "bug in bulk bias revocation");
> > > 
> > >   return status_code;
> > > }
> > > ```
> > >
> > > JVM 基于一种启发式的做法判断是否应该触发批量撤销或批量重偏向。
> > >
> > > 依赖三个阈值作出判断：
> > >
> > > ```bash
> > > # 批量重偏向阈值
> > > -XX:BiasedLockingBulkRebiasThreshold=20
> > > # 重置计数的延迟时间
> > > -XX:BiasedLockingDecayTime=25000
> > > # 批量撤销阈值
> > > -XX:BiasedLockingBulkRevokeThreshold=40
> > > ```
> > >
> > > 对于一个类（计数器和计时器都在这个类中，而非对象内），按上述三个参数而言：
> > >
> > > - 单个撤销的计数达到20，将会进行批量重偏向。
> > > - 距上次批量重偏向25ms即25s内，单次撤销计数达到40，就会发生批量撤销。
> > > - 每隔>=25s，会重置[20,40)之内的计数，这意味着可以多次批量重偏向。
> > >
> > > 源码 [biasedLocking.cpp](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/biasedLocking.cpp#l268)
> > >
> > > ```c++
> > > static HeuristicsResult update_heuristics(oop o, bool allow_rebias) {
> > >   markOop mark = o->mark();
> > >   if (!mark->has_bias_pattern()) {
> > >     return HR_NOT_BIASED;
> > >   }
> > > 
> > >   // Heuristics to attempt to throttle the number of revocations.
> > >   // Stages:
> > >   // 1. Revoke the biases of all objects in the heap of this type,
> > >   //    but allow rebiasing of those objects if unlocked.
> > >   // 2. Revoke the biases of all objects in the heap of this type
> > >   //    and don't allow rebiasing of these objects. Disable
> > >   //    allocation of objects of that type with the bias bit set.
> > >   Klass* k = o->klass();
> > >   jlong cur_time = os::javaTimeMillis();
> > >   jlong last_bulk_revocation_time = k->last_biased_lock_bulk_revocation_time();
> > >   int revocation_count = k->biased_lock_revocation_count();
> > >   if ((revocation_count >= BiasedLockingBulkRebiasThreshold) &&
> > >       (revocation_count <  BiasedLockingBulkRevokeThreshold) &&
> > >       (last_bulk_revocation_time != 0) &&
> > >       (cur_time - last_bulk_revocation_time >= BiasedLockingDecayTime)) {
> > >     // This is the first revocation we've seen in a while of an
> > >     // object of this type since the last time we performed a bulk
> > >     // rebiasing operation. The application is allocating objects in
> > >     // bulk which are biased toward a thread and then handing them
> > >     // off to another thread. We can cope with this allocation
> > >     // pattern via the bulk rebiasing mechanism so we reset the
> > >     // klass's revocation count rather than allow it to increase
> > >     // monotonically. If we see the need to perform another bulk
> > >     // rebias operation later, we will, and if subsequently we see
> > >     // many more revocation operations in a short period of time we
> > >     // will completely disable biasing for this type.
> > >     k->set_biased_lock_revocation_count(0);
> > >     revocation_count = 0;
> > >   }
> > > 
> > >   // Make revocation count saturate just beyond BiasedLockingBulkRevokeThreshold
> > >   if (revocation_count <= BiasedLockingBulkRevokeThreshold) {
> > >     revocation_count = k->atomic_incr_biased_lock_revocation_count();
> > >   }
> > > 
> > >   if (revocation_count == BiasedLockingBulkRevokeThreshold) {
> > >     return HR_BULK_REVOKE;
> > >   }
> > > 
> > >   if (revocation_count == BiasedLockingBulkRebiasThreshold) {
> > >     return HR_BULK_REBIAS;
> > >   }
> > > 
> > >   return HR_SINGLE_REVOKE;
> > > }
> > > ```
> > >
> > > 
> >
> > *偏向锁保存线程ID是为了完成锁重入，避免同一进程连续操作时持续进行偏向锁获取*
>
> 轻量级锁
>
> >轻量级锁的获取
> >
> >>1. 当前进程创建lock record，将`_lock`赋值为对象的Mark Word，`_obj`指向对象
> >>
> >>2. 通过CAS操作，尝试将地址存储在对象头的Mark Word中。若对象处于（001）状态，则存储成功，代表该线程获得了该轻量级锁，并将（001）状态设置为（00）状态，说明对象已上了轻量级锁。若对象处于（00）状态，进入步骤3
> >>
> >>3. 若Mark Word中ptr_to_lock_record指向当前线程的lock record，则说明是一次锁重入，此时将设置lock record的`_lock`为null，起到重入计数器的作用；若不指向当前线程的lock record，说明其他线程已获取到该轻量级锁，进入步骤4
> >>
> >>4. 当前线程会使用自旋锁等待原轻量级锁线程释放锁。`-XX：+UseSpinning`用来开启自旋锁，默认下为开启状态。自旋时间过长反而会影响性能，因此通过`-XX：PreBlockSpin`来设置自旋次数，默认为10次。
> >>
> >>   *自适应自旋：*
> >>
> >>   JDK1.6引入了自适应自旋。自适应意味着自旋的时间不再固定了，而是由前一次在同一个锁上的自旋时间及锁的拥有者的状态来决定。之前自旋常成功，就增加自旋次数；之前自选少成功，就减少自旋次数，甚至取消。
> >>
> >>5. 若当前线程自旋到了次数，原轻量级锁线程依然没有释放锁，或者当前线程自旋未到次数，但出现了第三个线程来竞争锁对象。此时轻量级锁会**膨胀**为重量级锁。重量级锁会把除了原轻量级锁线程外的线程全部阻塞，防止CPU空转。
> >
> >轻量级锁的解锁
> >
> >>1. 遍历线程栈,找到所有`_obj`字段指向当前锁对象的lock record。
> >>2. 若lock record的`_lock`为null，代表此为重入，将`_obj`设置为null后continue。
> >>3. 若lockrecord的`_lock`不为null，则利用CAS指令将对象头的mark word恢复成为`_lock`。如果成功，则continue，否则**膨胀**为重量级锁。进入重量级锁释放阶段。
>
> 锁膨胀
>
> > 整个锁膨胀的过程是通过自旋(无意义的for循环)来完成的
> >
> > ```c++
> > ObjectMonitor * ATTR ObjectSynchronizer::inflate (Thread * Self, oop object) {
> > ...
> > 
> > // 空for循环实现CAS
> > for (;;) {
> >    const markOop mark = object->mark() ;
> >    assert (!mark->has_bias_pattern(), "invariant") ;
> > 
> >    // The mark can be in one of the following states:
> >    // *  Inflated     - just return
> >    // *  Stack-locked - coerce it to inflated
> >    // *  INFLATING    - busy wait for conversion to complete
> >    // *  Neutral      - aggressively inflate the object.
> >    // *  BIASED       - Illegal.  We should never see this
> > 
> >    // CASE: inflated
> >    if (mark->has_monitor()) {
> >        ObjectMonitor * inf = mark->monitor() ;
> >        ...
> >        return inf ;
> >    }
> > 
> >    // CASE: inflation in progress - inflating over a stack-lock.
> >    // Some other thread is converting from stack-locked to inflated.
> >    // Only that thread can complete inflation -- other threads must wait.
> >    // The INFLATING value is transient.
> >    // Currently, we spin/yield/park and poll the markword, waiting for inflation to finish.
> >    // We could always eliminate polling by parking the thread on some auxiliary list.
> >    // 有其他的线程正在从stack-locked转换为inflated
> >    // 且只有那个正在做的线程能够完成膨胀，其余线程都自旋等待其完成
> >    if (mark == markOopDesc::INFLATING()) {
> >       TEVENT (Inflate: spin while INFLATING) ;
> >       ReadStableMark(object) ;
> >       continue ;
> >    }
> > 
> >    // CASE: stack-locked
> >    // Could be stack-locked either by this thread or by some other thread.
> >    //
> >    // Note that we allocate the objectmonitor speculatively, _before_ attempting
> >    // to install INFLATING into the mark word.  We originally installed INFLATING,
> >    // allocated the objectmonitor, and then finally STed the address of the
> >    // objectmonitor into the mark.  This was correct, but artificially lengthened
> >    // the interval in which INFLATED appeared in the mark, thus increasing
> >    // the odds of inflation contention.
> >    //
> >    // We now use per-thread private objectmonitor free lists.
> >    // These list are reprovisioned from the global free list outside the
> >    // critical INFLATING...ST interval.  A thread can transfer
> >    // multiple objectmonitors en-mass from the global free list to its local free list.
> >    // This reduces coherency traffic and lock contention on the global free list.
> >    // Using such local free lists, it doesn't matter if the omAlloc() call appears
> >    // before or after the CAS(INFLATING) operation.
> >    // See the comments in omAlloc().
> >    // 我们现在使用每个线程私有objectmonitor自由列表。这些列表是从临界膨胀间隔外的	  
> >    // 全局自由列表中重新设置的。一个线程可以将多个objectmonitors从全局自由列表大	   
> >    // 量传输到其本地自由列表。这减少了全局空闲列表上的一致性通信量和锁争用。使用这	  
> >    // 样的本地空闲列表，omAlloc（）调用出现在CAS（膨胀）操作之前还是之后并不重		  
> >    // 要。请参见omAlloc（）中的注释。
> > 
> >    if (mark->has_locker()) {
> >        // 乐观的预测能CAS成功，因此直接分配ObjectMoniter
> >        // 每个线程都可以试着创建，但只有成功的会被保存，不然就会被释放
> >        ObjectMonitor * m = omAlloc (Self) ;
> >        // Optimistically prepare the objectmonitor - anticipate successful CAS
> >        // We do this before the CAS in order to minimize the length of time
> >        // in which INFLATING appears in the mark.
> >        m->Recycle();
> >        m->_Responsible  = NULL ;
> >        m->OwnerIsThread = 0 ;
> >        m->_recursions   = 0 ;
> >        m->_SpinDuration = ObjectMonitor::Knob_SpinLimit ;   // Consider: maintain by type/class
> > 
> >        markOop cmp = (markOop) Atomic::cmpxchg_ptr (markOopDesc::INFLATING(), object->mark_addr(), mark) ;
> >        if (cmp != mark) {
> >            // CAS失败的话，就释放空间，然后重试
> >           omRelease (Self, m, true) ;
> >           continue ;       // Interference -- just retry
> >        }
> > 
> >        // We've successfully installed INFLATING (0) into the mark-word.
> >        // This is the only case where 0 will appear in a mark-work.
> >        // Only the singular thread that successfully swings the mark-word
> >        // to 0 can perform (or more precisely, complete) inflation.
> >        //
> >        // Why do we CAS a 0 into the mark-word instead of just CASing the
> >        // mark-word from the stack-locked value directly to the new inflated state?
> >        // Consider what happens when a thread unlocks a stack-locked object.
> >        // It attempts to use CAS to swing the displaced header value from the
> >        // on-stack basiclock back into the object header.  Recall also that the
> >        // header value (hashcode, etc) can reside in (a) the object header, or
> >        // (b) a displaced header associated with the stack-lock, or (c) a displaced
> >        // header in an objectMonitor.  The inflate() routine must copy the header
> >        // value from the basiclock on the owner's stack to the objectMonitor, all
> >        // the while preserving the hashCode stability invariants.  If the owner
> >        // decides to release the lock while the value is 0, the unlock will fail
> >        // and control will eventually pass from slow_exit() to inflate.  The owner
> >        // will then spin, waiting for the 0 value to disappear.   Put another way,
> >        // the 0 causes the owner to stall if the owner happens to try to
> >        // drop the lock (restoring the header from the basiclock to the object)
> >        // while inflation is in-progress.  This protocol avoids races that might
> >        // would otherwise permit hashCode values to change or "flicker" for an object.
> >        // Critically, while object->mark is 0 mark->displaced_mark_helper() is stable.
> >        // 0 serves as a "BUSY" inflate-in-progress indicator.
> > 
> > 
> >        // fetch the displaced mark from the owner's stack.
> >        // The owner can't die or unwind past the lock while our INFLATING
> >        // object is in the mark.  Furthermore the owner can't complete
> >        // an unlock on the object, either.
> >        // 只有成功将状态设置为INFLATING(0)的那个线程可以完成整个膨胀
> >        markOop dmw = mark->displaced_mark_helper() ;
> >        assert (dmw->is_neutral(), "invariant") ;
> > 
> >        // Setup monitor fields to proper values -- prepare the monitor
> >        m->set_header(dmw) ;
> > 
> >        // Optimization: if the mark->locker stack address is associated
> >        // with this thread we could simply set m->_owner = Self and
> >        // m->OwnerIsThread = 1. Note that a thread can inflate an object
> >        // that it has stack-locked -- as might happen in wait() -- directly
> >        // with CAS.  That is, we can avoid the xchg-NULL .... ST idiom.
> >        m->set_owner(mark->locker());
> >        m->set_object(object);
> >        // TODO-FIXME: assert BasicLock->dhw != 0.
> > 
> >        // Must preserve store ordering. The monitor state must
> >        // be stable at the time of publishing the monitor address.
> >        guarantee (object->mark() == markOopDesc::INFLATING(), "invariant") ;
> >        object->release_set_mark(markOopDesc::encode(m));
> > 
> >        // Hopefully the performance counters are allocated on distinct cache lines
> >        // to avoid false sharing on MP systems ...
> >        if (ObjectMonitor::_sync_Inflations != NULL) ObjectMonitor::_sync_Inflations->inc() ;
> >        TEVENT(Inflate: overwrite stacklock) ;
> >        if (TraceMonitorInflation) {
> >          if (object->is_instance()) {
> >            ResourceMark rm;
> >            tty->print_cr("Inflating object " INTPTR_FORMAT " , mark " INTPTR_FORMAT " , type %s",
> >              (void *) object, (intptr_t) object->mark(),
> >              object->klass()->external_name());
> >          }
> >        }
> >        return m ;
> >    }
> > 
> >    // CASE: neutral
> >    // TODO-FIXME: for entry we currently inflate and then try to CAS _owner.
> >    // If we know we're inflating for entry it's better to inflate by swinging a
> >    // pre-locked objectMonitor pointer into the object header.   A successful
> >    // CAS inflates the object *and* confers ownership to the inflating thread.
> >    // In the current implementation we use a 2-step mechanism where we CAS()
> >    // to inflate and then CAS() again to try to swing _owner from NULL to Self.
> >    // An inflateTry() method that we could call from fast_enter() and slow_enter()
> >    // would be useful.
> > 
> >    assert (mark->is_neutral(), "invariant");
> >    ObjectMonitor * m = omAlloc (Self) ;
> >    // prepare m for installation - set monitor to initial state
> >    m->Recycle();
> >    m->set_header(mark);
> >    m->set_owner(NULL);
> >    m->set_object(object);
> >    m->OwnerIsThread = 1 ;
> >    m->_recursions   = 0 ;
> >    m->_Responsible  = NULL ;
> >    m->_SpinDuration = ObjectMonitor::Knob_SpinLimit ;       // consider: keep metastats by type/class
> > 
> >    if (Atomic::cmpxchg_ptr (markOopDesc::encode(m), object->mark_addr(), mark) != mark) {
> >        m->set_object (NULL) ;
> >        m->set_owner  (NULL) ;
> >        m->OwnerIsThread = 0 ;
> >        m->Recycle() ;
> >        omRelease (Self, m, true) ;
> >        m = NULL ;
> >        continue ;
> >        // interference - the markword changed - just retry.
> >        // The state-transitions are one-way, so there's no chance of
> >        // live-lock -- "Inflated" is an absorbing state.
> >    }
> > 
> >    // Hopefully the performance counters are allocated on distinct
> >    // cache lines to avoid false sharing on MP systems ...
> >    if (ObjectMonitor::_sync_Inflations != NULL) ObjectMonitor::_sync_Inflations->inc() ;
> >    TEVENT(Inflate: overwrite neutral) ;
> >    if (TraceMonitorInflation) {
> >      if (object->is_instance()) {
> >        ResourceMark rm;
> >        tty->print_cr("Inflating object " INTPTR_FORMAT " , mark " INTPTR_FORMAT " , type %s",
> >          (void *) object, (intptr_t) object->mark(),
> >          object->klass()->external_name());
> >      }
> >    }
> >    return m ;
> > }
> > }
> > ```
> >
> > - mark->has_monitor() 判断如果当前锁对象为重量级锁，也就是lock:10，则执行第二步骤,否则执行第三步骤。
> > - 通过 mark->monitor获得重量级锁的对象监视器ObjectMonitor并返回，锁膨胀过程结束。
> > - 如果当前锁处于 INFLATING,说明有其他线程在执行锁膨胀，那么当前线程通过自旋等待其他线程锁膨胀完成。
> > - 如果当前是轻量级锁状态 mark->has_locker()，has_locker()表示现在该对象处在轻量级锁的状态中。则进行锁膨胀。首先，通过omAlloc方法获得一个可用的ObjectMonitor，并设置初始数据；然后通过CAS将对象头设置为`markOopDesc:INFLATING`，表示当前锁正在膨胀，如果CAS失败，继续自旋。如果成功，就把这个ObjectMonitor的owner设置为之前拥有此对象轻量级锁的线程。
> > - 如果是无锁状态is_neutral()，逻辑类似第四步骤。只不过这里因为没有线程拥有此对象的轻量级锁，所以设置ObjectMonitor的owner为null。
> >
> > *锁膨胀的过程实际上是获得一个ObjectMonitor对象监视器，而真正抢占锁的逻辑，在 ObjectMonitor::enter方法里面。*
>
> 重量级锁
>
> > 锁竞争
> >
> > [ObjectMonitor::enter](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l318)
> >
> > ```c++
> > void ATTR ObjectMonitor::enter(TRAPS) {
> > // The following code is ordered to check the most common cases first
> > // and to reduce RTS->RTO cache line upgrades on SPARC and IA32 processors.
> > Thread * const Self = THREAD ;
> > void * cur ;
> > 
> > cur = Atomic::cmpxchg_ptr (Self, &_owner, NULL) ;
> > if (cur == NULL) {//CAS成功
> > // Either ASSERT _recursions == 0 or explicitly set _recursions = 0.
> > assert (_recursions == 0   , "invariant") ;
> > assert (_owner      == Self, "invariant") ;
> > // CONSIDER: set or assert OwnerIsThread == 1
> > return ;
> > }
> > 
> > if (cur == Self) {
> > // TODO-FIXME: check for integer overflow!  BUGID 6557169.
> > _recursions ++ ;
> > return ;
> > }
> > 
> > if (Self->is_lock_owned ((address)cur)) {
> > assert (_recursions == 0, "internal state error");
> > _recursions = 1 ;
> > // Commute owner from a thread-specific on-stack BasicLockObject address to
> > // a full-fledged "Thread *".
> > _owner = Self ;
> > OwnerIsThread = 1 ;
> > return ;
> > }
> > 
> > // We've encountered genuine contention.
> > assert (Self->_Stalled == 0, "invariant") ;
> > Self->_Stalled = intptr_t(this) ;
> > 
> > // Try one round of spinning *before* enqueueing Self
> > // and before going through the awkward and expensive state
> > // transitions.  The following spin is strictly optional ...
> > // Note that if we acquire the monitor from an initial spin
> > // we forgo posting JVMTI events and firing DTRACE probes.
> > if (Knob_SpinEarly && TrySpin (Self) > 0) {
> > assert (_owner == Self      , "invariant") ;
> > assert (_recursions == 0    , "invariant") ;
> > assert (((oop)(object()))->mark() == markOopDesc::encode(this), "invariant") ;
> > Self->_Stalled = 0 ;
> > return ;
> > }
> > 
> > assert (_owner != Self          , "invariant") ;
> > assert (_succ  != Self          , "invariant") ;
> > assert (Self->is_Java_thread()  , "invariant") ;
> > JavaThread * jt = (JavaThread *) Self ;
> > assert (!SafepointSynchronize::is_at_safepoint(), "invariant") ;
> > assert (jt->thread_state() != _thread_blocked   , "invariant") ;
> > assert (this->object() != NULL  , "invariant") ;
> > assert (_count >= 0, "invariant") ;
> > 
> > // Prevent deflation at STW-time.  See deflate_idle_monitors() and is_busy().
> > // Ensure the object-monitor relationship remains stable while there's contention.
> > Atomic::inc_ptr(&_count);
> > 
> > EventJavaMonitorEnter event;
> > 
> > { // Change java thread status to indicate blocked on monitor enter.
> > JavaThreadBlockedOnMonitorEnterState jtbmes(jt, this);
> > 
> > DTRACE_MONITOR_PROBE(contended__enter, this, object(), jt);
> > if (JvmtiExport::should_post_monitor_contended_enter()) {
> > JvmtiExport::post_monitor_contended_enter(jt, this);
> > }
> > 
> > OSThreadContendState osts(Self->osthread());
> > ThreadBlockInVM tbivm(jt);
> > 
> > Self->set_current_pending_monitor(this);
> > 
> > // TODO-FIXME: change the following for(;;) loop to straight-line code.
> > for (;;) {
> > jt->set_suspend_equivalent();
> > // cleared by handle_special_suspend_equivalent_condition()
> > // or java_suspend_self()
> > 
> > EnterI (THREAD) ;
> > 
> > 
> > if (!ExitSuspendEquivalent(jt)) break ;
> > 
> > //
> > // We have acquired the contended monitor, but while we were
> > // waiting another thread suspended us. We don't want to enter
> > // the monitor while suspended because that would surprise the
> > // thread that suspended us.
> > //
> > // 退出suspend状态，因为获取到monitor的时候，可能别的线程给我们挂起了
> > // 我们不希望进入monitor的时候还被挂起，因为这会surprise那个挂起我们的线程
> >     _recursions = 0 ;
> > _succ = NULL ;
> > exit (false, Self) ;
> > 
> > jt->java_suspend_self();
> > }
> > Self->set_current_pending_monitor(NULL);
> > }
> > ...//此处省略无数行代码
> > ```
> >
> > - 通过CAS将monitor的 _owner字段设置为当前线程，如果设置成功，则直接返回。
> > - 若之前的 `_owner`指向的是当前的线程，说明是重入，`_recursions++`增加重入次数。
> > - 如果当前线程获取监视器锁成功，将 `_recursions`设置为1， `_owner`设置为当前线程。
> > - 如果获取锁失败，则需要通过自旋等待锁释放，自旋执行方法ObjectMonitor::TrySpin()这个也就是TrySpin_VaryDuration()
> >
> > [ObjectMonitor::EnterI](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l502)
> >
> > ```c++
> > void ATTR ObjectMonitor::EnterI (TRAPS) {
> >     Thread * Self = THREAD ;
> >     ...//省略很多代码
> >     ObjectWaiter node(Self) ;
> >     Self->_ParkEvent->reset() ;
> >     node._prev   = (ObjectWaiter *) 0xBAD ;
> >     node.TState  = ObjectWaiter::TS_CXQ ;
> > 
> >     // Push "Self" onto the front of the _cxq.
> >     // Once on cxq/EntryList, Self stays on-queue until it acquires the lock.
> >     // Note that spinning tends to reduce the rate at which threads
> >     // enqueue and dequeue on EntryList|cxq.
> >     ObjectWaiter * nxt ;
> >     for (;;) { //自旋，将node添加到_cxq队列的头部
> >         node._next = nxt = _cxq ;
> >         if (Atomic::cmpxchg_ptr (&node, &_cxq, nxt) == nxt) break ;
> > 
> >         // Interference - the CAS failed because _cxq changed.  Just retry.
> >         // As an optional optimization we retry the lock.
> >         // 这里虽然添加队列失败了，但是，可以尝试直接获取锁一次，这也体现了
> >         // synchronized的非公平锁特点
> >         if (TryLock (Self) > 0) {
> >             assert (_succ != Self         , "invariant") ;
> >             assert (_owner == Self        , "invariant") ;
> >             assert (_Responsible != Self  , "invariant") ;
> >             return ;
> >         }
> >     }
> >     ...//省略很多代码
> >     //node节点添加到_cxq队列之后，继续通过自旋尝试获取锁，如果在指定的阈值范围内没有获得锁，则通过park将当前线程挂起，等待被唤醒
> >     for (;;) {
> >         if (TryLock (Self) > 0) break ;
> >         assert (_owner != Self, "invariant") ;
> > 
> >         if ((SyncFlags & 2) && _Responsible == NULL) {
> >            Atomic::cmpxchg_ptr (Self, &_Responsible, NULL) ;
> >         }
> > 
> >         // park self //通过park挂起当前线程
> >         if (_Responsible == Self || (SyncFlags & 1)) {
> >             TEVENT (Inflated enter - park TIMED) ;
> >             Self->_ParkEvent->park ((jlong) RecheckInterval) ;
> >             // Increase the RecheckInterval, but clamp the value.
> >             RecheckInterval *= 8 ;
> >             if (RecheckInterval > 1000) RecheckInterval = 1000 ;
> >         } else {
> >             TEVENT (Inflated enter - park UNTIMED) ;
> >             Self->_ParkEvent->park() ;//当前线程挂起
> >         }
> > 
> >         if (TryLock(Self) > 0) break ; //当线程被唤醒时，会从这里继续执行
> > 
> > 
> >         TEVENT (Inflated enter - Futile wakeup) ;
> >         if (ObjectMonitor::_sync_FutileWakeups != NULL) {
> >            ObjectMonitor::_sync_FutileWakeups->inc() ;
> >         }
> >         ++ nWakeups ;
> > 
> >         if ((Knob_SpinAfterFutile & 1) && TrySpin (Self) > 0) break ;
> > 
> >         if ((Knob_ResetEvent & 1) && Self->_ParkEvent->fired()) {
> >            Self->_ParkEvent->reset() ;
> >            OrderAccess::fence() ;
> >         }
> >         if (_succ == Self) _succ = NULL ;
> > 
> >         // Invariant: after clearing _succ a thread *must* retry _owner before parking.
> >         OrderAccess::fence() ;
> >     }
> >     ...//省略很多代码
> > }
> > ```
> >
> > - 将当前线程封装成ObjectWaiter对象node，状态设置成TS_CXQ。
> > - 通过自旋操作将node节点push到_cxq队列的头部。
> > - node节点添加到_cxq队列之后，继续通过自旋尝试获取锁，如果在指定的阈值范围内没有获得锁，则通过park将当前线程挂起，等待被唤醒。
> >
> >  [ObjectMonitor::TryLock](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l483)
> >
> > ```c++
> > int ObjectMonitor::TryLock (Thread * Self) {
> >    for (;;) {
> >       void * own = _owner ;
> >       if (own != NULL) return 0 ;
> >       if (Atomic::cmpxchg_ptr (Self, &_owner, NULL) == NULL) {
> >          // Either guarantee _recursions == 0 or set _recursions = 0.
> >          assert (_recursions == 0, "invariant") ;
> >          assert (_owner == Self, "invariant") ;
> >          // CONSIDER: set or assert that OwnerIsThread == 1
> >          return 1 ;
> >       }
> >       // The lock had been free momentarily, but we lost the race to the lock.
> >       // Interference -- the CAS failed.
> >       // We can either return -1 or retry.
> >       // Retry doesn't make as much sense because the lock was just acquired.
> >       if (true) return -1 ;
> >    }
> > }
> > ```
> >
> > CAS尝试设置monitor的`_owner`为当前线程。
> >
> > - 如果`_owner`本身不为空，说明已经有了owner拥有了此monitor，此时返回0
> > - 如果CAS操作返回为空，表明成功获得锁，返回1
> > - CAS操作失败，可以返回-1或重试，但重试意义不大，因为锁刚被别人获取，这是因为，第一个if判断了own为NULL，但是第二个if却没能CAS成功，说明在第一个if到第二个if之间，锁被别人获得了，那么，对于刚刚被别人获得的锁，即便再试一次，也大概率无法获得，因为不会那么快释放。
> >
> > Atomic::cmpxchg_ptr()
> >
> > ```c++
> > jbyte Atomic::cmpxchg(jbyte exchange_value, volatile jbyte* dest, jbyte compare_value){
> >     // exchange_value:要换成的新值
> >     // dest:比较的地址
> >     // compare_value:用来和dest进行比较的旧值
> >     ...
> > }
> > ```
> >
> > 锁释放
> >
> > [ObjectMonitor::exit](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l956)
> >
> > ```cpp
> > void ATTR ObjectMonitor::exit(bool not_suspended, TRAPS) {
> >    Thread * Self = THREAD ;
> >    if (THREAD != _owner) {//如果当前锁对象中的_owner没有指向当前线程
> >      //如果_owner指向的BasicLock在当前线程栈上,那么将_owner指向当前线程
> >      if (THREAD->is_lock_owned((address) _owner)) {
> >        // Transmute _owner from a BasicLock pointer to a Thread address.
> >        // We don't need to hold _mutex for this transition.
> >        // Non-null to Non-null is safe as long as all readers can
> >        // tolerate either flavor.
> >        assert (_recursions == 0, "invariant") ;
> >        _owner = THREAD ;
> >        _recursions = 0 ;
> >        OwnerIsThread = 1 ;
> >      } else {
> >        // NOTE: we need to handle unbalanced monitor enter/exit
> >        // in native code by throwing an exception.
> >        // TODO: Throw an IllegalMonitorStateException ?
> >        TEVENT (Exit - Throw IMSX) ;
> >        assert(false, "Non-balanced monitor enter/exit!");
> >        if (false) {
> >           THROW(vmSymbols::java_lang_IllegalMonitorStateException());
> >        }
> >        return;
> >      }
> >    }
> >    //如果当前，线程重入锁的次数，不为0，那么就重新走ObjectMonitor::exit，直到重入锁次数为0为止
> >    if (_recursions != 0) {
> >      _recursions--;        // this is simple recursive enter
> >      TEVENT (Inflated exit - recursive) ;
> >      return ;
> >    }
> >   ...//此处省略很多代码
> >   for (;;) {
> >     if (Knob_ExitPolicy == 0) {
> >       OrderAccess::release_store(&_owner, (void*)NULL);   //释放锁
> >       OrderAccess::storeload();                        // See if we need to wake a successor
> >       if ((intptr_t(_EntryList)|intptr_t(_cxq)) == 0 || _succ != NULL) {
> >         TEVENT(Inflated exit - simple egress);
> >         return;
> >       }
> >       TEVENT(Inflated exit - complex egress);
> >       //省略部分代码...
> >     }
> >     //省略部分代码...
> >     ObjectWaiter * w = NULL;
> >     int QMode = Knob_QMode;
> >     //根据QMode的模式判断，QMode分类唤醒机制：
> >     //如果QMode == 2则直接从_cxq挂起的线程中唤醒    
> >     if (QMode == 2 && _cxq != NULL) {
> >       w = _cxq;
> >       ExitEpilog(Self, w);
> >       return;
> >     }
> >     // QMode == 3 是把_cxq添加到_EntryList的尾巴上 _EntryList.append(_cxq)
> >       if (QMode == 3 && _cxq != NULL) {
> >           // 一有机会就把_cxq引入到EntryList中去
> >           // 批量转移_cxq到EntryList中去
> >           // 首先，分离_cxq
> >           // 下面的循环等于:w = swap (&cxq, NULL)
> >           
> >           w = _cxq ;
> >           // 将_cxq置空，然后w为原_cxq
> >           for (;;) {
> >              assert (w != NULL, "Invariant") ;
> >              ObjectWaiter * u = (ObjectWaiter *) Atomic::cmpxchg_ptr (NULL, &_cxq, w) ;
> >              if (u == w) break ;
> >              w = u ;
> >           }
> >           assert (w != NULL              , "invariant") ;
> > 
> >           // 因为_cxq本身是单向_next的，而_EntryList是双向的
> >           // 所以这里先把w处理成双向的，后面就可以一步拼接到位
> >           ObjectWaiter * q = NULL ;
> >           ObjectWaiter * p ;
> >           for (p = w ; p != NULL ; p = p->_next) {
> >               guarantee (p->TState == ObjectWaiter::TS_CXQ, "Invariant") ;
> >               p->TState = ObjectWaiter::TS_ENTER ;
> >               p->_prev = q ;
> >               q = p ;
> >           }
> > 
> >           // Append the RATs to the EntryList
> >           // TODO: organize EntryList as a CDLL so we can locate the tail in constant-time.
> >           
> >           ObjectWaiter * Tail ;
> >           // 先是一个for循环，将Tail定位到_EntryList的尾部
> >           for (Tail = _EntryList ; Tail != NULL && Tail->_next != NULL ; Tail = Tail->_next) ;
> >           // 如果Tail为NULL，说明_EntryList本身为NULL
> >           // 直接将其赋值为w
> >           // 反之，则将其添加到_EntryList的尾部
> >           // _EntryList是个双向循环列表，和_WaitSet一样，_cxq则是单向列表
> >           if (Tail == NULL) {
> >               _EntryList = w ;
> >           } else {
> >               Tail->_next = w ;
> >               w->_prev = Tail ;
> >           }
> > 
> >           // Fall thru into code that tries to wake a successor from EntryList
> >           // 进入试图从EntryList中唤醒successor的代码，在QMode == 4后面
> >       }
> >       
> >       // QMode == 4 是把_cxq添加到_EntryList的头部 _EntryList.preappend(_cxq)
> >       if (QMode == 4 && _cxq != NULL) {
> >           // Aggressively drain cxq into EntryList at the first opportunity.
> >           // This policy ensure that recently-run threads live at the head of EntryList.
> > 
> >           // Drain _cxq into EntryList - bulk transfer.
> >           // First, detach _cxq.
> >           // The following loop is tantamount to: w = swap (&cxq, NULL)
> >           w = _cxq ;
> >           for (;;) {
> >              assert (w != NULL, "Invariant") ;
> >              ObjectWaiter * u = (ObjectWaiter *) Atomic::cmpxchg_ptr (NULL, &_cxq, w) ;
> >              if (u == w) break ;
> >              w = u ;
> >           }
> >           assert (w != NULL              , "invariant") ;
> > 
> >           ObjectWaiter * q = NULL ;
> >           ObjectWaiter * p ;
> >           for (p = w ; p != NULL ; p = p->_next) {
> >               guarantee (p->TState == ObjectWaiter::TS_CXQ, "Invariant") ;
> >               p->TState = ObjectWaiter::TS_ENTER ;
> >               p->_prev = q ;
> >               q = p ;
> >           }
> > 
> >           // 前面的操作都一样，只有这里，是添加到头部
> >           // Prepend the RATs to the EntryList
> >           if (_EntryList != NULL) {
> >               q->_next = _EntryList ;
> >               _EntryList->_prev = q ;
> >           }
> >           _EntryList = w ;
> > 
> >           // Fall thru into code that tries to wake a successor from EntryList
> >       }
> >       
> > 	  // 到这里的时候
> >       // 1. _cxq为空，这样前三个if都进不去
> >       // 2. _cxq不为空，且QMode != 2
> >       
> >       // 优先从_EntryList中选取successor
> > 	  w = _EntryList  ;
> >       if (w != NULL) {
> >           // 1. _cxq为空，但_EntryList不为空会进入到这里
> >           // 2. _cxq不为空，若QMode == 3 || QMode == 4，
> >           // _EntryList一定就不为空了，所以一定进到这里
> >           
> >           // I'd like to write: guarantee (w->_thread != Self).
> >           // But in practice an exiting thread may find itself on the EntryList.
> >           // Lets say thread T1 calls O.wait().  Wait() enqueues T1 on O's waitset and
> >           // then calls exit().  Exit release the lock by setting O._owner to NULL.
> >           // Lets say T1 then stalls.  T2 acquires O and calls O.notify().  The
> >           // notify() operation moves T1 from O's waitset to O's EntryList. T2 then
> >           // release the lock "O".  T2 resumes immediately after the ST of null into
> >           // _owner, above.  T2 notices that the EntryList is populated, so it
> >           // reacquires the lock and then finds itself on the EntryList.
> >           // Given all that, we have to tolerate the circumstance where "w" is
> >           // associated with Self.
> >           // 上面整个注释在解视if里为什么是w!=NULL
> >           // 因为，一个刚刚释放锁的线程，
> >           // 可能从新进入EntryList中参与竞争，
> >           // 因此需要容忍w与自身可能相等，
> >           // 即便他本身想写guarantee (w->_thread != Self)
> >           assert (w->TState == ObjectWaiter::TS_ENTER, "invariant") ;
> >           // _EntryList不为空，直接从中选取唤醒线程
> >           ExitEpilog (Self, w) ;
> >           return ;
> >       }
> > 
> >       // If we find that both _cxq and EntryList are null then just
> >       // re-run the exit protocol from the top.
> >       // 在_EntryList为空的情况下
> >       // 如果_cxq还为空，说明没有任何线程在等待中，直接continue，re-run
> >       w = _cxq ;
> >       if (w == NULL) continue ;
> > 
> >       // 如果_cxq不为空，把_cxq添加入_EntryList中
> >       // Drain _cxq into EntryList - bulk transfer.
> >       // First, detach _cxq.
> >       // The following loop is tantamount to: w = swap (&cxq, NULL)
> >       // 依然是先将_cxq置空
> >       for (;;) {
> >           assert (w != NULL, "Invariant") ;
> >           ObjectWaiter * u = (ObjectWaiter *) Atomic::cmpxchg_ptr (NULL, &_cxq, w) ;
> >           if (u == w) break ;
> >           w = u ;
> >       }
> >       TEVENT (Inflated exit - drain cxq into EntryList) ;
> > 
> >       assert (w != NULL              , "invariant") ;
> >       assert (_EntryList  == NULL    , "invariant") ;
> > 
> >       // Convert the LIFO SLL anchored by _cxq into a DLL.
> >       // The list reorganization step operates in O(LENGTH(w)) time.
> >       // It's critical that this step operate quickly as
> >       // "Self" still holds the outer-lock, restricting parallelism
> >       // and effectively lengthening the critical section.
> >       // Invariant: s chases t chases u.
> >       // TODO-FIXME: consider changing EntryList from a DLL to a CDLL so
> >       // we have faster access to the tail.
> > 
> >       // 把LIFO的_cxq变成双向链表
> >       // 这一步要快，因为“Self”仍拥有着外部所，限制了并行效率并延长了临界截面
> >       
> >       // QMode == 1 把_cxq反转且变成双向链表后赋值给_EntryList
> >       if (QMode == 1) {
> >          // QMode == 1 : drain cxq to EntryList, reversing order
> >          // We also reverse the order of the list.
> >          ObjectWaiter * s = NULL ;
> >          ObjectWaiter * t = w ;
> >          ObjectWaiter * u = NULL ;
> >          while (t != NULL) {
> >              guarantee (t->TState == ObjectWaiter::TS_CXQ, "invariant") ;
> >              t->TState = ObjectWaiter::TS_ENTER ;
> >              u = t->_next ;
> >              t->_prev = u ;
> >              t->_next = s ;
> >              s = t;
> >              t = u ;
> >          }
> >          _EntryList  = s ;
> >          assert (s != NULL, "invariant") ;
> >       } else {
> >          // QMode != 1 把_cxq变成双向链表后赋值给_EntryList
> >          // QMode == 0 or QMode == 2
> >          _EntryList = w ;
> >          ObjectWaiter * q = NULL ;
> >          ObjectWaiter * p ;
> >          for (p = w ; p != NULL ; p = p->_next) {
> >              guarantee (p->TState == ObjectWaiter::TS_CXQ, "Invariant") ;
> >              p->TState = ObjectWaiter::TS_ENTER ;
> >              p->_prev = q ;
> >              q = p ;
> >          }
> >       }
> > 
> >       // In 1-0 mode we need: ST EntryList; MEMBAR #storestore; ST _owner = NULL
> >       // The MEMBAR is satisfied by the release_store() operation in ExitEpilog().
> > 
> >       // See if we can abdicate to a spinner instead of waking a thread.
> >       // A primary goal of the implementation is to reduce the
> >       // context-switch rate.
> >       
> >       // 从_EntryList为空到这里为止，都只是在挪_cxq到_EntryList
> >       // 这里准备唤醒，但唤醒前先判断是否有自旋成功的线程将自己设置为_succ
> >       // 如果成功，就不去唤醒线程，因为这样可以降低上下文切换率，提高效率
> >       if (_succ != NULL) continue;
> > 
> >       w = _EntryList  ;
> >       if (w != NULL) {
> >           guarantee (w->TState == ObjectWaiter::TS_ENTER, "invariant") ;
> >           ExitEpilog (Self, w) ;
> >           return ;
> >       }
> >       
> >   }
> > }
> > ```
> >
> > - _cxq：栈
> > - _EntryList：双向不循环链表
> > - _WaitSet：双向循环链表
> >
> > exit()的逻辑树：**QMode == 0（默认）**
> >
> > 1. _cxq != null **(first)**
> >    1. QMode == 2   ExitEpilog(_cxq); ***return;***
> >    2. QMode == 3   _EntryList.append(Dequeue( _cxq)); ***TO 2***
> >    3. QMode == 4   _EntryList.preappend(Dequeue( _cxq)); ***TO 2***_
> > 2. EntryList != null  ExitEpilog(_EntryList); ***return;***    ***因为QMode默认为0，所以 _EntryList不为空的话，默认会走到这里***
> > 3. _EntryList == null
> >    1. _cxq == null **(Second)**  ***Continue; 这表明两个都是空的，没有可以唤醒的线程***  
> >    2. _cxq != null **(Second)** && QMode == 1   _EntryList = Dequeue( _cxq).reverse(); ***TO 3.4***
> >    3. _cxq != null **(Second)** && QMode != 1   _EntryList = Dequeue( _cxq); ***TO 3.4*** ***因为QMode默认为0，所以 _EntryList 为空的话，默认会走到这里***
> >    4. _succ != null ***Continue;*** else ***TO 3.5***
> >    5. _EntryList != null   ExitEpilog( _EntryList); ***return;***
> >
> > 唤醒线程实际发生在[ObjectMonitor::ExitEpilog](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l1327)
> >
> > ```c++
> > void ObjectMonitor::ExitEpilog (Thread * Self, ObjectWaiter * Wakee) {
> >    // QMode == 2 时 传进来的Wakee是_cxq
> >    // QMode != 2 时 传进来的Wakee是_EntryList
> >     assert (_owner == Self, "invariant") ;
> > 
> >    // Exit protocol:
> >    // 1. ST _succ = wakee
> >    // 2. membar #loadstore|#storestore;
> >    // 2. ST _owner = NULL
> >    // 3. unpark(wakee)
> > 
> >    // 设置_succ = wakee
> >    _succ = Knob_SuccEnabled ? Wakee->_thread : NULL ;
> >    ParkEvent * Trigger = Wakee->_event ;
> > 
> >    // Hygiene -- once we've set _owner = NULL we can't safely dereference Wakee again.
> >    // The thread associated with Wakee may have grabbed the lock and "Wakee" may be
> >    // out-of-scope (non-extant).
> >    // 置空Wakee
> >    Wakee  = NULL ;
> > 
> >    // Drop the lock
> >    // 设置_owner == NULL
> >    OrderAccess::release_store_ptr (&_owner, NULL) ;
> >    OrderAccess::fence() ;                               // ST _owner vs LD in unpark()
> > 
> >    if (SafepointSynchronize::do_call_back()) {
> >       TEVENT (unpark before SAFEPOINT) ;
> >    }
> > 
> >    // 唤醒_succ unpark(Wakee)
> >    DTRACE_MONITOR_PROBE(contended__exit, this, object(), Self);
> >    Trigger->unpark() ;
> > 
> >    // Maintain stats and report events to JVMTI
> >    if (ObjectMonitor::_sync_Parks != NULL) {
> >       ObjectMonitor::_sync_Parks->inc() ;
> >    }
> > }
> > ```
> >
> > exit()完成需要完成以下步骤
> >
> > - 设置_succ = Wakee
> > - 设置_owner = NULL
> > - 唤醒_succ unpark(Wakee)



## Object: wait() & notify()/notifyAll()

> wait()
>
> > [objectMonitor::wait](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l1463)
> >
> > ```c++
> > // wait-notify-reenter 三段协议
> > void ObjectMonitor::wait(jlong millis, bool interruptible, TRAPS) {
> >    // 这里到了wait阶段
> >    
> >    // 将当前线程命名为Self，且Self必须是Java线程
> >    Thread * const Self = THREAD ;
> >    assert(Self->is_Java_thread(), "Must be Java thread!");
> >    JavaThread *jt = (JavaThread *)THREAD;
> > 
> >    // 初始化
> >    DeferredInitialize () ;
> > 
> >    // Throw IMSX or IEX.
> >    // 检查_owner
> >    CHECK_OWNER();
> > 
> >    EventJavaMonitorWait event;
> > 
> >    // 检查挂起的中断
> >    // check for a pending interrupt
> >    if (interruptible && Thread::is_interrupted(Self, true) && !HAS_PENDING_EXCEPTION) {
> >      // post monitor waited event.  Note that this is past-tense, we are done waiting.
> >      if (JvmtiExport::should_post_monitor_waited()) {
> >         // Note: 'false' parameter is passed here because the
> >         // wait was not timed out due to thread interrupt.
> >         JvmtiExport::post_monitor_waited(jt, this, false);
> > 
> >         // In this short circuit of the monitor wait protocol, the
> >         // current thread never drops ownership of the monitor and
> >         // never gets added to the wait queue so the current thread
> >         // cannot be made the successor. This means that the
> >         // JVMTI_EVENT_MONITOR_WAITED event handler cannot accidentally
> >         // consume an unpark() meant for the ParkEvent associated with
> >         // this ObjectMonitor.
> >         // 在监视器等待协议的这种短路中，当前线程永远不会放弃监视器的所有权，
> >         // 也永远不会被添加到等待队列中，因此当前线程不能成为后继线程。 
> >         // 这意味着JVMTI_EVENT_MONITOR_WAITED事件处理程序不会意外消耗
> >         // 用于与此ObjectMonitor关联的ParkEvent的unpark（）。
> >      }
> >      if (event.should_commit()) {
> >        post_monitor_wait_event(&event, 0, millis, false);
> >      }
> >      TEVENT (Wait - Throw IEX) ;
> >      THROW(vmSymbols::java_lang_InterruptedException());
> >      return ;
> >    }
> > 
> >    TEVENT (Wait) ;
> > 
> >    assert (Self->_Stalled == 0, "invariant") ;
> >    Self->_Stalled = intptr_t(this) ;
> >    jt->set_current_waiting_monitor(this);
> > 
> >    // create a node to be put into the queue
> >    // Critically, after we reset() the event but prior to park(), we must check
> >    // for a pending interrupt.
> >    // 将Self创建为一个用于加入到_WaitSet中的节点
> >    ObjectWaiter node(Self);
> >    node.TState = ObjectWaiter::TS_WAIT ;
> >    Self->_ParkEvent->reset() ;
> >    OrderAccess::fence();          // ST into Event; membar ; LD interrupted-flag
> > 
> >    // Enter the waiting queue, which is a circular doubly linked list in this case
> >    // but it could be a priority queue or any data structure.
> >    // _WaitSetLock protects the wait queue.  Normally the wait queue is accessed only
> >    // by the the owner of the monitor *except* in the case where park()
> >    // returns because of a timeout of interrupt.  Contention is exceptionally rare
> >    // so we use a simple spin-lock instead of a heavier-weight blocking lock.
> > 
> >    // 输入等待队列，在这种情况下，它是一个循环的双向链接列表，
> >    // 但可以是优先级队列或任何数据结构。 
> >    // _WaitSetLock保护等待队列。 
> >    // 通常，在park（）由于中断超时而返回的情况下，
> >    // 只有监视器的所有者* except *可以访问等待队列。 
> >    // 竞争非常罕见，因此我们使用简单的自旋锁而不是较重的阻塞锁。
> >    Thread::SpinAcquire (&_WaitSetLock, "WaitSet - add") ;
> >    AddWaiter (&node) ;
> >    Thread::SpinRelease (&_WaitSetLock) ;
> > 
> >    if ((SyncFlags & 4) == 0) {
> >       _Responsible = NULL ;
> >    }
> >    intptr_t save = _recursions; // record the old recursion count
> >    _waiters++;                  // increment the number of waiters
> >    _recursions = 0;             // set the recursion level to be 1
> >    exit (true, Self) ;                    // exit the monitor
> >    guarantee (_owner != Self, "invariant") ;
> > 
> >    // The thread is on the WaitSet list - now park() it.
> >    // On MP systems it's conceivable that a brief spin before we park
> >    // could be profitable.
> >    //
> >    // TODO-FIXME: change the following logic to a loop of the form
> >    //   while (!timeout && !interrupted && _notified == 0) park()
> > 
> >    // Self已经在WaitSet中了，现在挂起它
> >    // 在MP系统上，可以想象在park之前短暂旋转可能会有利可图。
> >    int ret = OS_OK ;
> >    int WasNotified = 0 ;
> >    { // State transition wrappers
> >      OSThread* osthread = Self->osthread();
> >      OSThreadWaitState osts(osthread, true);
> >      {
> >        ThreadBlockInVM tbivm(jt);
> >        // Thread is in thread_blocked state and oop access is unsafe.
> >        jt->set_suspend_equivalent();
> > 
> >        if (interruptible && (Thread::is_interrupted(THREAD, false) || HAS_PENDING_EXCEPTION)) {
> >            // Intentionally empty
> >        } else
> >        if (node._notified == 0) {
> >          // 根据millis进行挂起
> >          if (millis <= 0) {
> >             Self->_ParkEvent->park () ;
> >          } else {
> >             ret = Self->_ParkEvent->park (millis) ;
> >          }
> >        }
> > 
> >        // were we externally suspended while we were waiting?
> >        if (ExitSuspendEquivalent (jt)) {
> >           // TODO-FIXME: add -- if succ == Self then succ = null.
> >           jt->java_suspend_self();
> >        }
> > 
> >      } // Exit thread safepoint: transition _thread_blocked -> _thread_in_vm
> > 
> >      // 这里到了notify阶段
> > 
> >      // Node may be on the WaitSet, the EntryList (or cxq), or in transition
> >      // from the WaitSet to the EntryList.
> >      // See if we need to remove Node from the WaitSet.
> >      // We use double-checked locking to avoid grabbing _WaitSetLock
> >      // if the thread is not on the wait queue.
> >      //
> >      // Note that we don't need a fence before the fetch of TState.
> >      // In the worst case we'll fetch a old-stale value of TS_WAIT previously
> >      // written by the is thread. (perhaps the fetch might even be satisfied
> >      // by a look-aside into the processor's own store buffer, although given
> >      // the length of the code path between the prior ST and this load that's
> >      // highly unlikely).  If the following LD fetches a stale TS_WAIT value
> >      // then we'll acquire the lock and then re-fetch a fresh TState value.
> >      // That is, we fail toward safety.
> > 
> >      if (node.TState == ObjectWaiter::TS_WAIT) {
> >          Thread::SpinAcquire (&_WaitSetLock, "WaitSet - unlink") ;
> >          if (node.TState == ObjectWaiter::TS_WAIT) {
> >             DequeueSpecificWaiter (&node) ;       // unlink from WaitSet
> >             assert(node._notified == 0, "invariant");
> >             node.TState = ObjectWaiter::TS_RUN ;
> >          }
> >          Thread::SpinRelease (&_WaitSetLock) ;
> >      }
> > 
> >      // The thread is now either on off-list (TS_RUN),
> >      // on the EntryList (TS_ENTER), or on the cxq (TS_CXQ).
> >      // The Node's TState variable is stable from the perspective of this thread.
> >      // No other threads will asynchronously modify TState.
> >      guarantee (node.TState != ObjectWaiter::TS_WAIT, "invariant") ;
> >      OrderAccess::loadload() ;
> >      if (_succ == Self) _succ = NULL ;
> >      WasNotified = node._notified ;
> > 
> >      // 这里到了reenter阶段
> >      
> >      // Reentry phase -- reacquire the monitor.
> >      // re-enter contended monitor after object.wait().
> >      // retain OBJECT_WAIT state until re-enter successfully completes
> >      // Thread state is thread_in_vm and oop access is again safe,
> >      // although the raw address of the object may have changed.
> >      // (Don't cache naked oops over safepoints, of course).
> > 
> >      // post monitor waited event. Note that this is past-tense, we are done waiting.
> >      if (JvmtiExport::should_post_monitor_waited()) {
> >        JvmtiExport::post_monitor_waited(jt, this, ret == OS_TIMEOUT);
> > 
> >        if (node._notified != 0 && _succ == Self) {
> >          // In this part of the monitor wait-notify-reenter protocol it
> >          // is possible (and normal) for another thread to do a fastpath
> >          // monitor enter-exit while this thread is still trying to get
> >          // to the reenter portion of the protocol.
> >          //
> >          // The ObjectMonitor was notified and the current thread is
> >          // the successor which also means that an unpark() has already
> >          // been done. The JVMTI_EVENT_MONITOR_WAITED event handler can
> >          // consume the unpark() that was done when the successor was
> >          // set because the same ParkEvent is shared between Java
> >          // monitors and JVM/TI RawMonitors (for now).
> >          //
> >          // We redo the unpark() to ensure forward progress, i.e., we
> >          // don't want all pending threads hanging (parked) with none
> >          // entering the unlocked monitor.
> >            
> >          // 在监视器的wait-notify-reenter协议的这一部分中，
> >          // 另一个线程有可能（并且正常）
> >          // 在该线程仍试图到达协议的reenter部分时
> >          // 执行快速路径监视器的进入-退出。
> >          //
> >          // 已通知ObjectMonitor，当前线程是后继线程，
> >          // 这也意味着unpark（）已经完成。 
> >          // JVMTI_EVENT_MONITOR_WAITED事件处理程序可以
> >          // 使用设置继任者时执行的unpark（），
> >          // 因为在Java监视器和JVM / TI RawMonitor之间
> >          // 共享同一ParkEvent（目前）。
> >          //
> >          // 我们重做unpark（）以确保前进，
> >          // 即，我们不希望所有挂起的线程挂起（驻留），而没有线程进入未锁定的监视器。
> >          node._event->unpark();
> >        }
> >      }
> > 
> >      if (event.should_commit()) {
> >        post_monitor_wait_event(&event, node._notifier_tid, millis, ret == OS_TIMEOUT);
> >      }
> > 
> >      OrderAccess::fence() ;
> > 
> >      assert (Self->_Stalled != 0, "invariant") ;
> >      Self->_Stalled = 0 ;
> > 
> >      assert (_owner != Self, "invariant") ;
> >      ObjectWaiter::TStates v = node.TState ;
> >      // 如果在TS_RUN状态，enter()
> >      // 如果在TS_ENTER(_EntryList)或者TS_CXQ(_cxq)状态，ReenterI()
> >      if (v == ObjectWaiter::TS_RUN) {
> >          enter (Self) ;
> >      } else {
> >          guarantee (v == ObjectWaiter::TS_ENTER || v == ObjectWaiter::TS_CXQ, "invariant") ;
> >          ReenterI (Self, &node) ;
> >          node.wait_reenter_end(this);
> >      }
> > 
> >      // Self has reacquired the lock.
> >      // Lifecycle - the node representing Self must not appear on any queues.
> >      // Node is about to go out-of-scope, but even if it were immortal we wouldn't
> >      // want residual elements associated with this thread left on any lists.
> >      // Self已经重新获得锁了
> >      // 生命周期——表示Self的节点不能出现在任何队列上。
> >      // Node即将超出作用域，但即使它是不朽的，我们也不会
> >      // 希望与该线程相关的剩余元素留在任何列表中。
> >      guarantee (node.TState == ObjectWaiter::TS_RUN, "invariant") ;
> >      assert    (_owner == Self, "invariant") ;
> >      assert    (_succ != Self , "invariant") ;
> >    } // OSThreadWaitState()
> > 
> >    jt->set_current_waiting_monitor(NULL);
> > 
> >    guarantee (_recursions == 0, "invariant") ;
> >    // 重载旧重入数
> >    _recursions = save;     // restore the old recursion count
> >    // 减少waiters
> >    _waiters--;             // decrement the number of waiters
> > 
> >    // Verify a few postconditions
> >    assert (_owner == Self       , "invariant") ;
> >    assert (_succ  != Self       , "invariant") ;
> >    assert (((oop)(object()))->mark() == markOopDesc::encode(this), "invariant") ;
> > 
> >    if (SyncFlags & 32) {
> >       OrderAccess::fence() ;
> >    }
> > 
> >    // check if the notification happened
> >    // 检查通知是否发生
> >    if (!WasNotified) {
> >      // no, it could be timeout or Thread.interrupt() or both
> >      // check for interrupt event, otherwise it is timeout
> >      // 没有的话，它可能是timeout或Thread.interrupt()或两者都是
> >      // 检查中断事件，否则为超时
> >      if (interruptible && Thread::is_interrupted(Self, true) && !HAS_PENDING_EXCEPTION) {
> >        TEVENT (Wait - throw IEX from epilog) ;
> >        THROW(vmSymbols::java_lang_InterruptedException());
> >      }
> >    }
> > 
> >    // NOTE: Spurious wake up will be consider as timeout.
> >    // Monitor notify has precedence over thread interrupt.
> >    // 注意：虚假的唤醒将被视为超时。
> >    // 监视器通知优先于线程中断。
> > }
> > ```
> >
> > - wait
> >   - 将Self包装成一个node添加到_WaitSet中。添加前后需要通过CAS获取和释放WaitSetLock。
> >   - 对Self的重入次数进行保留，同时增加_waiters的数值，并设置重入数为0。
> >   - 退出管程
> >   - 通过park()挂起线程
> > - notify
> >   - 被notify的时候，可能在 _WaitSet或 _EntryList或 _cxq上。
> >   - 通过double-check（检查node.TState == ObjectWaiter::TS_WAIT )来判断，如果是在 _WaitSet 上，则首先将其从 _WaitSet 上去除，并将node.TState设置为ObjectWaiter::TS_RUN
> >
> > - reenter
> >   - 如果被通知，且自己被选为_succ，则通过unpark()唤醒
> >   - 如果node.TState == ObjectWaiter::TS_RUN，enter()
> >   - 如果在 _cxq 或者 _EntryList 中，则ReenterI()
> >   - 上面步骤执行后，Self已经获得锁，则可以重载之前保存的重入数值，同时减少_waiters的数值
> >
> > [objectMonitor::CHECK_OWNER()](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l1416)
> >
> > ```c++
> > #define CHECK_OWNER()                                                             \
> >   do {                                                                            \
> >     if (THREAD != _owner) {                                                       \
> >       if (THREAD->is_lock_owned((address) _owner)) {                              \
> >         _owner = THREAD ;  /* Convert from basiclock addr to Thread addr */       \
> >         _recursions = 0;                                                          \
> >         OwnerIsThread = 1 ;                                                       \
> >       } else {                                                                    \
> >         TEVENT (Throw IMSX) ;                                                     \
> >         THROW(vmSymbols::java_lang_IllegalMonitorStateException());               \
> >       }                                                                           \
> >     }                                                                             \
> >   } while (false)
> > ```
> >
> > -  _owner == Self   ***return;***
> > - _owner != Self && _owner指向Self的Lock Record   改 _owner指向Self; ***return;***
> > - _owner != Self && _owner不指向Self的Lock Record   ***THROW IMSX;***
> >
> > [AddWaiter(ObjectWaiter* node)](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l2328) 添加节点到_WaitSet中
> >
> > ```c++
> > inline void ObjectMonitor::AddWaiter(ObjectWaiter* node) {
> >   assert(node != NULL, "should not dequeue NULL node");
> >   assert(node->_prev == NULL, "node already in list");
> >   assert(node->_next == NULL, "node already in list");
> >   // put node at end of queue (circular doubly linked list)
> >   if (_WaitSet == NULL) {
> >     // 只有一个节点的时候，双向指针都指向自己，自双向循环
> >     _WaitSet = node;
> >     node->_prev = node;
> >     node->_next = node;
> >   } else {
> >     // 双向循环链表添加节点操作
> >     ObjectWaiter* head = _WaitSet ;
> >     ObjectWaiter* tail = head->_prev;
> >     assert(tail->_next == head, "invariant check");
> >     tail->_next = node;
> >     head->_prev = node;
> >     node->_next = head;
> >     node->_prev = tail;
> >   }
> >   // 证明_WaitSet是双向循环链表
> > }
> > ```
> >
> > [objectMonitor::ReenterI](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l751)
> >
> > ```c++
> > // ReenterI（）是EnterI（）的竞争慢路径后半段的专用内联形式。
> > // 我们仅将ReenterI（）用于wait（）中的监视器重入。
> > // 将来，我们应该协调EnterI（）和ReenterI（），添加Knob_Reset和Knob_SpinAfterFutile支持并相应地重构循环。
> > void ATTR ObjectMonitor::ReenterI (Thread * Self, ObjectWaiter * SelfNode) {
> >     assert (Self != NULL                , "invariant") ;
> >     assert (SelfNode != NULL            , "invariant") ;
> >     assert (SelfNode->_thread == Self   , "invariant") ;
> >     assert (_waiters > 0                , "invariant") ;
> >     assert (((oop)(object()))->mark() == markOopDesc::encode(this) , "invariant") ;
> >     assert (((JavaThread *)Self)->thread_state() != _thread_blocked, "invariant") ;
> >     JavaThread * jt = (JavaThread *) Self ;
> > 
> >     int nWakeups = 0 ;
> >     for (;;) {
> >         ObjectWaiter::TStates v = SelfNode->TState ;
> >         guarantee (v == ObjectWaiter::TS_ENTER || v == ObjectWaiter::TS_CXQ, "invariant") ;
> >         assert    (_owner != Self, "invariant") ;
> > 
> >         if (TryLock (Self) > 0) break ;
> >         if (TrySpin (Self) > 0) break ;
> > 
> >         TEVENT (Wait Reentry - parking) ;
> > 
> >         // State transition wrappers around park() ...
> >         // ReenterI() wisely defers state transitions until
> >         // it's clear we must park the thread.
> >         // 围绕park()的状态转换包装器。
> >         // ReenterI（）明智地推迟了状态转换，直到很明显我们必须停放线程为止。
> >         {
> >            OSThreadContendState osts(Self->osthread());
> >            ThreadBlockInVM tbivm(jt);
> > 
> >            // cleared by handle_special_suspend_equivalent_condition()
> >            // or java_suspend_self()
> >            jt->set_suspend_equivalent();
> >            if (SyncFlags & 1) {
> >               Self->_ParkEvent->park ((jlong)1000) ;
> >            } else {
> >               Self->_ParkEvent->park () ;
> >            }
> > 
> >            // were we externally suspended while we were waiting?
> >            for (;;) {
> >               if (!ExitSuspendEquivalent (jt)) break ;
> >               if (_succ == Self) { _succ = NULL; OrderAccess::fence(); }
> >               jt->java_suspend_self();
> >               jt->set_suspend_equivalent();
> >            }
> >         }
> > 
> >         // Try again, but just so we distinguish between futile wakeups and
> >         // successful wakeups.  The following test isn't algorithmically
> >         // necessary, but it helps us maintain sensible statistics.
> >         // 再试一次，但是只是这样我们才能区分徒劳的唤醒和成功的唤醒。
> >         // 以下测试不是算法上必需的，但是可以帮助我们维护合理的统计信息。
> >         if (TryLock(Self) > 0) break ;
> > 
> >         // The lock is still contested.
> >         // Keep a tally of the # of futile wakeups.
> >         // Note that the counter is not protected by a lock or updated by atomics.
> >         // That is by design - we trade "lossy" counters which are exposed to
> >         // races during updates for a lower probe effect.
> >         // 仍存在锁争用，记录徒劳唤醒的次数
> >         TEVENT (Wait Reentry - futile wakeup) ;
> >         ++ nWakeups ;
> > 
> >         // Assuming this is not a spurious wakeup we'll normally
> >         // find that _succ == Self.
> >         // 假设这不是一个虚假的觉醒，我们通常会发现_succ == Self
> >         if (_succ == Self) _succ = NULL ;
> > 
> >         // Invariant: after clearing _succ a contending thread
> >         // *must* retry  _owner before parking.
> >         OrderAccess::fence() ;
> > 
> >         if (ObjectMonitor::_sync_FutileWakeups != NULL) {
> >           ObjectMonitor::_sync_FutileWakeups->inc() ;
> >         }
> >     }
> > 
> >     // Self has acquired the lock -- Unlink Self from the cxq or EntryList .
> >     // Normally we'll find Self on the EntryList.
> >     // Unlinking from the EntryList is constant-time and atomic-free.
> >     // From the perspective of the lock owner (this thread), the
> >     // EntryList is stable and cxq is prepend-only.
> >     // The head of cxq is volatile but the interior is stable.
> >     // In addition, Self.TState is stable.
> > 
> >     // Self已经获得锁 -- 从cxq或EntryList中Unlink Self
> >     // 通常情况下，我们会在EntryList中发现Self
> >     // 从入口列表中断开链接是固定时间和无原子的。
> >     // 从锁所有者(这个线程)的角度来看EntryList是稳定的，cxq是preend -only的。
> >     // cxq的头部是不稳定的，但内部是稳定的。
> >     // 此外,Self.TState是稳定的。
> >     assert (_owner == Self, "invariant") ;
> >     assert (((oop)(object()))->mark() == markOopDesc::encode(this), "invariant") ;
> >     UnlinkAfterAcquire (Self, SelfNode) ;
> >     if (_succ == Self) _succ = NULL ;
> >     assert (_succ != Self, "invariant") ;
> >     SelfNode->TState = ObjectWaiter::TS_RUN ;
> >     OrderAccess::fence() ;      // see comments at the end of EnterI()
> > }
> > ```
>
> notify()
>
> > [objectMonitor.cpp::notify](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l1698)
> >
> > ```c++
> > void ObjectMonitor::notify(TRAPS) {
> >   CHECK_OWNER();
> >   // 如果_WaitSet为空，则无对象唤醒，直接返回
> >   if (_WaitSet == NULL) {
> >      TEVENT (Empty-Notify) ;
> >      return ;
> >   }
> >   DTRACE_MONITOR_PROBE(notify, this, object(), THREAD);
> > 
> >   int Policy = Knob_MoveNotifyee ;
> > 
> >   Thread::SpinAcquire (&_WaitSetLock, "WaitSet - notify") ;
> >   ObjectWaiter * iterator = DequeueWaiter() ; // 获取唤醒对象
> >   if (iterator != NULL) {
> >       TEVENT (Notify1 - Transfer) ;
> >      guarantee (iterator->TState == ObjectWaiter::TS_WAIT, "invariant") ;
> >      guarantee (iterator->_notified == 0, "invariant") ;
> >      // Policy != 4的情况下，把iterator->TState设置为ObjectWaiter::TS_ENTER 
> >      if (Policy != 4) {
> >         iterator->TState = ObjectWaiter::TS_ENTER ;
> >      }
> >      iterator->_notified = 1 ;
> >      Thread * Self = THREAD;
> >      iterator->_notifier_tid = Self->osthread()->thread_id();
> > 
> >      ObjectWaiter * List = _EntryList ;
> >      if (List != NULL) {
> >         assert (List->_prev == NULL, "invariant") ;
> >         assert (List->TState == ObjectWaiter::TS_ENTER, "invariant") ;
> >         assert (List != iterator, "invariant") ;
> >      }
> > 	 
> >      // Policy == 0 把WaitSet中的第一个添加到EntryList的头部
> >      if (Policy == 0) {       // prepend to EntryList
> >          if (List == NULL) {
> >              iterator->_next = iterator->_prev = NULL ;
> >              _EntryList = iterator ;
> >          } else {
> >              List->_prev = iterator ;
> >              iterator->_next = List ;
> >              iterator->_prev = NULL ;
> >              _EntryList = iterator ;
> >         }
> >      } else
> >      // Policy == 1 把WaitSet中的第一个添加到EntryList的尾部
> >      if (Policy == 1) {      // append to EntryList
> >          if (List == NULL) {
> >              iterator->_next = iterator->_prev = NULL ;
> >              _EntryList = iterator ;
> >          } else {
> >             // CONSIDER:  finding the tail currently requires a linear-time walk of
> >             // the EntryList.  We can make tail access constant-time by converting to
> >             // a CDLL instead of using our current DLL.
> >             ObjectWaiter * Tail ;
> >             for (Tail = List ; Tail->_next != NULL ; Tail = Tail->_next) ;
> >             assert (Tail != NULL && Tail->_next == NULL, "invariant") ;
> >             Tail->_next = iterator ;
> >             iterator->_prev = Tail ;
> >             iterator->_next = NULL ;
> >         }
> >      } else
> >      // Policy == 2 把WaitSet中的第一个添加到cxq的头部
> >      if (Policy == 2) {      // prepend to cxq
> >          // prepend to cxq
> >          if (List == NULL) {
> >              iterator->_next = iterator->_prev = NULL ;
> >              _EntryList = iterator ;
> >          } else {
> >             iterator->TState = ObjectWaiter::TS_CXQ ;
> >             for (;;) {
> >                 ObjectWaiter * Front = _cxq ;
> >                 iterator->_next = Front ;
> >                 if (Atomic::cmpxchg_ptr (iterator, &_cxq, Front) == Front) {
> >                     break ;
> >                 }
> >             }
> >          }
> >      } else
> >      // Policy == 3 把WaitSet中的第一个添加到cxq的尾部
> >      if (Policy == 3) {      // append to cxq
> >         iterator->TState = ObjectWaiter::TS_CXQ ;
> >         for (;;) {
> >             ObjectWaiter * Tail ;
> >             Tail = _cxq ;
> >             if (Tail == NULL) {
> >                 iterator->_next = NULL ;
> >                 if (Atomic::cmpxchg_ptr (iterator, &_cxq, NULL) == NULL) {
> >                    break ;
> >                 }
> >             } else {
> >                 while (Tail->_next != NULL) Tail = Tail->_next ;
> >                 Tail->_next = iterator ;
> >                 iterator->_prev = Tail ;
> >                 iterator->_next = NULL ;
> >                 break ;
> >             }
> >         }
> >      } 
> >      // Policy != 0,1,2,3 继续挂起WaitSet中的第一个
> >       else {
> >         ParkEvent * ev = iterator->_event ;
> >         iterator->TState = ObjectWaiter::TS_RUN ;
> >         OrderAccess::fence() ;
> >         ev->unpark() ;
> >      }
> > 
> >      if (Policy < 4) {
> >        iterator->wait_reenter_begin(this);
> >      }
> > 
> >      // _WaitSetLock protects the wait queue, not the EntryList.  We could
> >      // move the add-to-EntryList operation, above, outside the critical section
> >      // protected by _WaitSetLock.  In practice that's not useful.  With the
> >      // exception of  wait() timeouts and interrupts the monitor owner
> >      // is the only thread that grabs _WaitSetLock.  There's almost no contention
> >      // on _WaitSetLock so it's not profitable to reduce the length of the
> >      // critical section.
> >      // 除了wait（）超时和中断外，监视器所有者是获取_WaitSetLock的唯一线程。 
> >      // _WaitSetLock上几乎没有争用，因此缩短关键部分的长度并不有利。
> >   }
> > 
> >   Thread::SpinRelease (&_WaitSetLock) ;
> > 
> >   if (iterator != NULL && ObjectMonitor::_sync_Notifications != NULL) {
> >      ObjectMonitor::_sync_Notifications->inc() ;
> >   }
> > }
> > ```
> >
> > - 取_WaitSet中的第一个来作为待操作线程
> > - Policy == 0   添加到_EntryList的头部
> > - Policy == 1   添加到_EntryList的尾部
> > - **Policy == 2（默认）**   
> >   - _EntryList == NULL   _EntryList = iterator
> >   - _EntryList != NULL   添加到 _cxq的头部
> > - Policy == 3   添加到_cxq的尾部
> >
> > [DequeueWaiter()](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l2348) 获取_WaitSet头部并移除
> >
> > ```c++
> > inline ObjectWaiter* ObjectMonitor::DequeueWaiter() {
> >   // dequeue the very first waiter
> >   // 因为这里是获取的双向循环链表的头，也就是_WaitSet指针指向的对象，
> >   // 所以结合上一函数源码可知，notify的是最早加入_WaitSet的线程
> >   // 也就是说notyfy在hotSpot实现中，是先入先出唤醒的
> >   
> >   // 但这并不影响synchronized是非公平锁
> >   // 因为公平与否看的是：如果有一个新的线程想要获取锁，此时，它还未被加入_EntryList
> >   // 但是它却可以和List中的其它线程一起进行竞争，那这就是非公平锁
> >   // 因为新线程明明比List中的其他线程来的晚，却拥有了参与直接竞争的权力
> >   // 这对于其他的县城而言，是不公平的
> >   // 而List中线程是否是FIFO，不影响锁的公平性
> >     
> >   // 总结：
> >   // 公平锁：线程间按序竞争
> >   // 非公平锁：线程间随机竞争
> >   ObjectWaiter* waiter = _WaitSet;
> >   if (waiter) {
> >     DequeueSpecificWaiter(waiter); // 获取唤醒对象，并对_WaitSet进行删减处理
> >   }
> >   return waiter;
> > }
> > ```
> >
> > [DequeueSpecificWaiter(ObjectWaiter* node)](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l2357) 将node从_WaitSet中移除
> >
> > ```c++
> > inline void ObjectMonitor::DequeueSpecificWaiter(ObjectWaiter* node) {
> >   assert(node != NULL, "should not dequeue NULL node");
> >   assert(node->_prev != NULL, "node already removed from list");
> >   assert(node->_next != NULL, "node already removed from list");
> >   // when the waiter has woken up because of interrupt,
> >   // timeout or other spurious wake-up, dequeue the
> >   // waiter from waiting list
> >   ObjectWaiter* next = node->_next;
> >   if (next == node) {
> >     // 这里是_WaitSet中只有一个等待线程的情况，直接将其置空，表示删除
> >     assert(node->_prev == node, "invariant check");
> >     _WaitSet = NULL;
> >   } else {
> >     // 从_WaitSet中删除当前待唤醒线程对象node
> >     ObjectWaiter* prev = node->_prev;
> >     assert(prev->_next == node, "invariant check");
> >     assert(next->_prev == node, "invariant check");
> >     next->_prev = prev;
> >     prev->_next = next;
> >     if (_WaitSet == node) {
> >       _WaitSet = next;
> >     }
> >   }
> >   // 对node进行处理，使其与_WaitSet断绝关系
> >   node->_next = NULL;
> >   node->_prev = NULL;
> > }
> > ```
> >
> > [ObjectWaiter::wait_reenter_begin](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/objectMonitor.cpp#l2318)
> >
> > ```c++
> > void ObjectWaiter::wait_reenter_begin(ObjectMonitor *mon) {
> >   JavaThread *jt = (JavaThread *)this->_thread;
> >     // 这里究竟去哪了，干了什么，我不知道了。。。
> >   _active = JavaThreadBlockedOnMonitorEnterState::wait_reenter_begin(jt, mon);
> > }
> > ```
> >
> > 
> >
> > - **notify()**本身从_WaitSet中取待唤醒线程（在HotSpot的实现中）是**FIFO**的
> > - **notify()或者notifyAll()调用时并不会真正释放对象锁, 必须等到synchronized方法或者语法块执行完才真正释放锁.**（不过这个是默认策略，可以修改，在notify之后立马唤醒相关线程。）
> > - **synchronized**是**非公平锁**
> >
> > 
> >
> > 情况一：锁的获取与notify()唤醒顺序不同，可能随机
> >
> > ```java
> > for(int i=0;i<50;i++){
> >    synchronized (lock) {
> >      lock.notify();
> >    }
> > }
> > ```
> >
> > 这是因为notify()之后，会按线程wait()的顺序进行唤醒，但是main线程本身也会参与对lock对象的锁竞争。
>
> notifyAll()
>
> > 近乎重复notify()
> >
> > - 取_WaitSet中的第一个来作为待操作线程
> > - Policy == 0   添加到_EntryList的头部
> > - Policy == 1   添加到_EntryList的尾部
> > - **Policy == 2（默认）** 添加到 _cxq的头部（此处与notify()不同）
> > - Policy == 3   添加到_cxq的尾部



## Thread: exit() & join()

> [exit()](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/thread.cpp#l1729)
>
> > ```c++
> > void JavaThread::exit(bool destroy_vm, ExitType exit_type) {
> >   assert(this == JavaThread::current(),  "thread consistency check");
> > 
> >   HandleMark hm(this);
> >   Handle uncaught_exception(this, this->pending_exception());
> >   this->clear_pending_exception();
> >   Handle threadObj(this, this->threadObj());
> >   assert(threadObj.not_null(), "Java thread object should be created");
> > 
> >   if (get_thread_profiler() != NULL) {
> >     get_thread_profiler()->disengage();
> >     ResourceMark rm;
> >     get_thread_profiler()->print(get_thread_name());
> >   }
> > 
> > 
> >   // FIXIT: This code should be moved into else part, when reliable 1.2/1.3 check is in place
> >   {
> >     EXCEPTION_MARK;
> > 
> >     CLEAR_PENDING_EXCEPTION;
> >   }
> >   // FIXIT: The is_null check is only so it works better on JDK1.2 VM's. This
> >   // has to be fixed by a runtime query method
> >   if (!destroy_vm || JDK_Version::is_jdk12x_version()) {
> >     // JSR-166: change call from from ThreadGroup.uncaughtException to
> >     // java.lang.Thread.dispatchUncaughtException
> >     if (uncaught_exception.not_null()) {
> >       Handle group(this, java_lang_Thread::threadGroup(threadObj()));
> >       {
> >         EXCEPTION_MARK;
> >         // Check if the method Thread.dispatchUncaughtException() exists. If so
> >         // call it.  Otherwise we have an older library without the JSR-166 changes,
> >         // so call ThreadGroup.uncaughtException()
> >         KlassHandle recvrKlass(THREAD, threadObj->klass());
> >         CallInfo callinfo;
> >         KlassHandle thread_klass(THREAD, SystemDictionary::Thread_klass());
> >         LinkResolver::resolve_virtual_call(callinfo, threadObj, recvrKlass, thread_klass,
> >                                            vmSymbols::dispatchUncaughtException_name(),
> >                                            vmSymbols::throwable_void_signature(),
> >                                            KlassHandle(), false, false, THREAD);
> >         CLEAR_PENDING_EXCEPTION;
> >         methodHandle method = callinfo.selected_method();
> >         if (method.not_null()) {
> >           JavaValue result(T_VOID);
> >           JavaCalls::call_virtual(&result,
> >                                   threadObj, thread_klass,
> >                                   vmSymbols::dispatchUncaughtException_name(),
> >                                   vmSymbols::throwable_void_signature(),
> >                                   uncaught_exception,
> >                                   THREAD);
> >         } else {
> >           KlassHandle thread_group(THREAD, SystemDictionary::ThreadGroup_klass());
> >           JavaValue result(T_VOID);
> >           JavaCalls::call_virtual(&result,
> >                                   group, thread_group,
> >                                   vmSymbols::uncaughtException_name(),
> >                                   vmSymbols::thread_throwable_void_signature(),
> >                                   threadObj,           // Arg 1
> >                                   uncaught_exception,  // Arg 2
> >                                   THREAD);
> >         }
> >         if (HAS_PENDING_EXCEPTION) {
> >           ResourceMark rm(this);
> >           jio_fprintf(defaultStream::error_stream(),
> >                 "\nException: %s thrown from the UncaughtExceptionHandler"
> >                 " in thread \"%s\"\n",
> >                 pending_exception()->klass()->external_name(),
> >                 get_thread_name());
> >           CLEAR_PENDING_EXCEPTION;
> >         }
> >       }
> >     }
> > 
> >     // Called before the java thread exit since we want to read info
> >     // from java_lang_Thread object
> >     EventThreadEnd event;
> >     if (event.should_commit()) {
> >         event.set_javalangthread(java_lang_Thread::thread_id(this->threadObj()));
> >         event.commit();
> >     }
> > 
> >     // Call after last event on thread
> >     EVENT_THREAD_EXIT(this);
> > 
> >     // Call Thread.exit(). We try 3 times in case we got another Thread.stop during
> >     // the execution of the method. If that is not enough, then we don't really care. Thread.stop
> >     // is deprecated anyhow.
> >     if (!is_Compiler_thread()) {
> >       int count = 3;
> >       while (java_lang_Thread::threadGroup(threadObj()) != NULL && (count-- > 0)) {
> >         EXCEPTION_MARK;
> >         JavaValue result(T_VOID);
> >         KlassHandle thread_klass(THREAD, SystemDictionary::Thread_klass());
> >         JavaCalls::call_virtual(&result,
> >                               threadObj, thread_klass,
> >                               vmSymbols::exit_method_name(),
> >                               vmSymbols::void_method_signature(),
> >                               THREAD);
> >         CLEAR_PENDING_EXCEPTION;
> >       }
> >     }
> >     // notify JVMTI
> >     if (JvmtiExport::should_post_thread_life()) {
> >       JvmtiExport::post_thread_end(this);
> >     }
> > 
> >     // We have notified the agents that we are exiting, before we go on,
> >     // we must check for a pending external suspend request and honor it
> >     // in order to not surprise the thread that made the suspend request.
> >     while (true) {
> >       {
> >         MutexLockerEx ml(SR_lock(), Mutex::_no_safepoint_check_flag);
> >         if (!is_external_suspend()) {
> >           set_terminated(_thread_exiting);
> >           ThreadService::current_thread_exiting(this);
> >           break;
> >         }
> >         // Implied else:
> >         // Things get a little tricky here. We have a pending external
> >         // suspend request, but we are holding the SR_lock so we
> >         // can't just self-suspend. So we temporarily drop the lock
> >         // and then self-suspend.
> >       }
> > 
> >       ThreadBlockInVM tbivm(this);
> >       java_suspend_self();
> > 
> >       // We're done with this suspend request, but we have to loop around
> >       // and check again. Eventually we will get SR_lock without a pending
> >       // external suspend request and will be able to mark ourselves as
> >       // exiting.
> >     }
> >     // no more external suspends are allowed at this point
> >   } else {
> >     // before_exit() has already posted JVMTI THREAD_END events
> >   }
> > 
> >   // Notify waiters on thread object. This has to be done after exit() is called
> >   // on the thread (if the thread is the last thread in a daemon ThreadGroup the
> >   // group should have the destroyed bit set before waiters are notified).
> >   ensure_join(this);
> >   assert(!this->has_pending_exception(), "ensure_join should have cleared");
> > 
> >   // 6282335 JNI DetachCurrentThread spec states that all Java monitors
> >   // held by this thread must be released.  A detach operation must only
> >   // get here if there are no Java frames on the stack.  Therefore, any
> >   // owned monitors at this point MUST be JNI-acquired monitors which are
> >   // pre-inflated and in the monitor cache.
> >   //
> >   // ensure_join() ignores IllegalThreadStateExceptions, and so does this.
> >   if (exit_type == jni_detach && JNIDetachReleasesMonitors) {
> >     assert(!this->has_last_Java_frame(), "detaching with Java frames?");
> >     ObjectSynchronizer::release_monitors_owned_by_thread(this);
> >     assert(!this->has_pending_exception(), "release_monitors should have cleared");
> >   }
> > 
> >   // These things needs to be done while we are still a Java Thread. Make sure that thread
> >   // is in a consistent state, in case GC happens
> >   assert(_privileged_stack_top == NULL, "must be NULL when we get here");
> > 
> >   if (active_handles() != NULL) {
> >     JNIHandleBlock* block = active_handles();
> >     set_active_handles(NULL);
> >     JNIHandleBlock::release_block(block);
> >   }
> > 
> >   if (free_handle_block() != NULL) {
> >     JNIHandleBlock* block = free_handle_block();
> >     set_free_handle_block(NULL);
> >     JNIHandleBlock::release_block(block);
> >   }
> > 
> >   // These have to be removed while this is still a valid thread.
> >   remove_stack_guard_pages();
> > 
> >   if (UseTLAB) {
> >     tlab().make_parsable(true);  // retire TLAB
> >   }
> > 
> >   if (JvmtiEnv::environments_might_exist()) {
> >     JvmtiExport::cleanup_thread(this);
> >   }
> > 
> >   // We must flush any deferred card marks before removing a thread from
> >   // the list of active threads.
> >   Universe::heap()->flush_deferred_store_barrier(this);
> >   assert(deferred_card_mark().is_empty(), "Should have been flushed");
> > 
> > #if INCLUDE_ALL_GCS
> >   // We must flush the G1-related buffers before removing a thread
> >   // from the list of active threads. We must do this after any deferred
> >   // card marks have been flushed (above) so that any entries that are
> >   // added to the thread's dirty card queue as a result are not lost.
> >   if (UseG1GC) {
> >     flush_barrier_queues();
> >   }
> > #endif // INCLUDE_ALL_GCS
> > 
> >   // Remove from list of active threads list, and notify VM thread if we are the last non-daemon thread
> >   Threads::remove(this);
> > }
> > ```
> >
> > 其中有一句，专门用来唤醒在Thread类上的waiters
> >
> > ```c++
> >   // Notify waiters on thread object. This has to be done after exit() is called
> >   // on the thread (if the thread is the last thread in a daemon ThreadGroup the
> >   // group should have the destroyed bit set before waiters are notified).
> >   ensure_join(this);
> > ```
> >
> > [ensure_join()](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/share/vm/runtime/thread.cpp#l1709)
> >
> > ```c++
> > static void ensure_join(JavaThread* thread) {
> >   // We do not need to grap the Threads_lock, since we are operating on ourself.
> >   Handle threadObj(thread, thread->threadObj());
> >   assert(threadObj.not_null(), "java thread object must exist");
> >   ObjectLocker lock(threadObj, thread);
> >   // Ignore pending exception (ThreadDeath), since we are exiting anyway
> >   thread->clear_pending_exception();
> >   // Thread is exiting. So set thread_status field in  java.lang.Thread class to TERMINATED.
> >   java_lang_Thread::set_thread_status(threadObj(), java_lang_Thread::TERMINATED);
> >   // Clear the native thread instance - this makes isAlive return false and allows the join()
> >   // to complete once we've done the notify_all below
> >   java_lang_Thread::set_thread(threadObj(), NULL);
> >   lock.notify_all(thread);
> >   // Ignore pending exception (ThreadDeath), since we are exiting anyway
> >   thread->clear_pending_exception();
> > }
> > ```
> >
> > 其中强调了，会调用objectLocker::notify_all()方法
>
> 





