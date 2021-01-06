package designPattern.Proxy;

public class staticProxy implements work{
    work wk;

    public staticProxy(work wk){
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
