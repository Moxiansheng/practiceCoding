package designPattern.Proxy;

import designPattern.Proxy.Work;

public class StaticProxy implements Work {
    Work wk;

    public StaticProxy(Work wk){
        this.wk = wk;
    }

    @Override
    public void happyMarry() {
        before();
        wk.happyMarry();
        after();
    }

    private void after() {
        System.out.println("Exhausted");
    }

    private void before() {
        System.out.println("Excited");
    }
}
