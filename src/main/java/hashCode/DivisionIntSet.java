package hashCode;

public class DivisionIntSet extends LinkedIntSet4 {
    /**
     * 除法散列法
     */

    protected int H(int value) {
        return value % 10;
    }
}
