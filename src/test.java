import reflection.*;
import sort.*;
import dataStructure.*;
import hashCode.*;

import java.util.Scanner;

public class test {
    private static String interval = "============================================================";
    private static int type = 4;

    public static void main(String[] args) {
        //Scanner sc = new Scanner(System.in);
        // type = sc.nextInt();
        /*
        0:sort
        1:dataStructure
        2:reflection
        3:equals()
        4:hashCode()
        5:判断质数算法，哪个更快
        */
        if(type == 0){
            int[] array = {22, 34, 3, -32, 82, 55, 89, -50, 37, -5, 64, 35, 9, 70};
            base sort = new bucket(array, false, 2);
            sort.sort();
            sort.show();
        }else if(type == 1){
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
        }else if(type == 2){
            try{
                String classPath = "reflection.reflectedClass";
                reflectClass.reflectNewInstance(classPath);
                reflectClass.reflectPrivateConstructor(classPath);
                reflectClass.reflectPrivateField(classPath);
                reflectClass.reflectPrivateMethod(classPath);

                reflectedClass rc = reflectClass.reflectNewInstanceT(reflectedClass.class);
                rc.setName(classPath);
                rc.setAuthor(classPath);
                System.out.println(rc.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(type == 3){
            String a = "ab";
            String b = "ab";
            System.out.println(a.equals(b));

            try{
                reflectedClass rc1 = reflectClass.reflectNewInstanceB(reflectedClass.class, a, b);
                reflectedClass rc2 = reflectClass.reflectNewInstanceT(reflectedClass.class);
                rc2.setName(a);
                rc2.setAuthor(b);
                System.out.println(rc1.equals(rc2));
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(type == 4){
            singleIntSet set;
            int times = 1;

            intervalLine(times++);
            set = new singleIntSet();
            set.Add(3);
            set.Add(7);
            System.out.println(set.Contains(3)); // 输出 true
            System.out.println(set.Contains(5)); // 输出 false

            intervalLine(times++);

            set = new singleIntSet2();
            set.Add(13);
            set.Add(17);
            System.out.println(set.Contains(13)); // 输出 true
            System.out.println(set.Contains(15)); // 输出 false

            intervalLine(times++);

            set = new singleIntSet3();
            set.Add(3);
            System.out.println(set.Contains(3));
            set.Add(13);
            System.out.println(set.Contains(3));

            intervalLine(times++);

            set = new singleIntSet4();
            set.Add(3);
            System.out.println(set.Contains(3));
            set.Add(13);
            System.out.println(set.Contains(3));

            intervalLine(times++);

            set = new singleIntSet5();
            set.Add(3);
            System.out.println(set.Contains(3));
            set.Add(13);
            System.out.println(set.Contains(3));

            intervalLine(times++);

            set = new singleIntSet6(9);
            set.Add(3);
            System.out.println(set.Contains(3));
            set.Add(13);
            System.out.println(set.Contains(3));

            intervalLine(times++);

            set = new singleIntSet7(9);
            set.Add(3);
            System.out.println(set.Contains(3));
            set.Add(13);
            System.out.println(set.Contains(3));
        }else if(type == 5){
            long start = System.nanoTime();
            common.common.isPrime1(999999999);
            long end = System.nanoTime();
            System.out.println(end - start);

            intervalLine(0);

            start = System.nanoTime();
            common.common.isPrime2(999999999);
            end = System.nanoTime();
            System.out.println(end - start);
        }
    }

    private static void intervalLine(int times){
        System.out.println(times + "  " + interval);
    }
}