import reflection.reflectClass;
import reflection.reflectedClass;
import sort.*;
import dataStructure.heap;

import java.util.HashSet;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        //Scanner sc = new Scanner(System.in);
        // int type = sc.nextInt();
        int type = 3;
        /*
        0:sort
        1:dataStructure
        2:reflection
        3:equals()
        4:hashCode()
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
            String a = new String("ab");
            String b = new String("ab");
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
            HashSet<Integer> hs = new HashSet<>();

        }
    }
}