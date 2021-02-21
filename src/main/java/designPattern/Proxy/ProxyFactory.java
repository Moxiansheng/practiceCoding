package designPattern.Proxy;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;
import java.util.Vector;


public class ProxyFactory {
    public static Object getJDKProxy(Object target){
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(), // 目标类的类加载
                target.getClass().getInterfaces(),  // 代理需要实现的接口，可指定多个
                new JDKDynamicProxy(target)   // 代理对象对应的自定义 InvocationHandler
        );
    }

    public static Object getCGLIBProxy(Class<?> clazz) {
        // 创建动态代理增强类
        Enhancer enhancer = new Enhancer();
        // 设置类加载器
        enhancer.setClassLoader(clazz.getClassLoader());
        // 设置被代理类
        enhancer.setSuperclass(clazz);
        // 设置方法拦截器
        enhancer.setCallback(new CGLIBDynamicProxy());
        // 创建代理类
        return enhancer.create();
    }
}
