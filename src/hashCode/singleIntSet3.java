package hashCode;

public class singleIntSet3 extends singleIntSet {
    protected int H(int value) {
        return value <= 9 ? value : value - 10;
    }
}
