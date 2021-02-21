package invCov;

import java.util.ArrayList;
import java.util.List;

public class InvCov {
    private static <T> void test(List<? super T> dst, List<T> src){
        for (T t : src) {
            dst.add(t);
        }
    }

    public static void main(String[] args) {
        List<RedApple> src = new ArrayList<>();
        src.add(new SmallRedApple());
        List<Apple> dst = new ArrayList<>();
        test(dst, src);
    }
}
