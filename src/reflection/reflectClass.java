package reflection;

import javax.management.InstanceNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class reflectClass {
    private final static String TAG = "reflection.reflectClass";
    private static String name = "Java";
    private static String author = "Mo";
    private static String newInstance = "reflectNewInstance: ";
    private static String privateConstructor = "reflectPrivateConstructor: ";
    private static String privateField = "reflectPrivateField: ";

    public static void reflectNewInstance(String classPath){
        try{
            Class<?> classBook = Class.forName(classPath);
            Object objectBook = classBook.newInstance();
            reflectedClass book = (reflectedClass) objectBook;
            book.setName(name);
            book.setAuthor(author);
            System.out.println(newInstance + book.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void reflectPrivateConstructor(String classPath){
        try{
            Class<?> classBook = Class.forName(classPath);
            Constructor<?> declaredConstructorBook = classBook.getDeclaredConstructor(String.class, String.class);
            declaredConstructorBook.setAccessible(true);
            Object objectBook = declaredConstructorBook.newInstance(name, author);
            reflectedClass book = (reflectedClass) objectBook;
            System.out.println(privateConstructor + book.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void reflectPrivateField(String classPath){
        try{
            Class<?> classBook = Class.forName(classPath);
            Object objectBook = classBook.newInstance();
            Field fieldTag = classBook.getDeclaredField("TAG");
            fieldTag.setAccessible(true);
            String tag = (String) fieldTag.get(objectBook);
            System.out.println("reflectPrivateField: " + tag);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void reflectPrivateMethod(String classPath){
        try{
            Class<?> classBook = Class.forName(classPath);
            Method methodBook = classBook.getDeclaredMethod("declaredMethod", int.class);
            methodBook.setAccessible(true);
            Object objectBook = classBook.newInstance();
            String string = (String) methodBook.invoke(objectBook, 0);
            System.out.println("reflectPrivateMethod: " + string);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //==================================================================================================================

    public static reflectedClass reflectNewInstanceB(Class bClass, String name, String author){
        try{
            reflectedClass book = (reflectedClass) bClass.newInstance();
            book.setName(name);
            book.setAuthor(author);
            return book;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void reflectPrivateConstructorB(String classPath){
        try{
            Class<?> classBook = Class.forName(classPath);
            Constructor<?> declaredConstructorBook = classBook.getDeclaredConstructor(String.class, String.class);
            declaredConstructorBook.setAccessible(true);
            Object objectBook = declaredConstructorBook.newInstance(name, author);
            reflectedClass book = (reflectedClass) objectBook;
            System.out.println(privateConstructor + book.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void reflectPrivateFieldB(String classPath){
        try{
            Class<?> classBook = Class.forName(classPath);
            Object objectBook = classBook.newInstance();
            Field fieldTag = classBook.getDeclaredField("TAG");
            fieldTag.setAccessible(true);
            String tag = (String) fieldTag.get(objectBook);
            System.out.println("reflectPrivateField: " + tag);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void reflectPrivateMethodB(String classPath){
        try{
            Class<?> classBook = Class.forName(classPath);
            Method methodBook = classBook.getDeclaredMethod("declaredMethod", int.class);
            methodBook.setAccessible(true);
            Object objectBook = classBook.newInstance();
            String string = (String) methodBook.invoke(objectBook, 0);
            System.out.println("reflectPrivateMethod: " + string);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //==================================================================================================================

    public static <T> T reflectNewInstanceT(Class<T> classT) throws IllegalAccessException, InstantiationException {
        return classT.newInstance();
    }
}
