package mutilThread;

import java.util.Random;
import java.util.concurrent.Callable;

// 可以定义返回值；可以抛出异常
public class testThread5 implements Callable<Integer> {
    private int seed = 0;

    public testThread5(int seed){
        this.seed = seed;
    }
    @Override
    public Integer call() throws Exception {
        return new Random(seed++).nextInt();
    }
}
