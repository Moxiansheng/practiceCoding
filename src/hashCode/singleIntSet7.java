package hashCode;

public class singleIntSet7 extends singleIntSet6 {
    private double A = Integer.MAX_VALUE / Math.pow(2, 32);
    public singleIntSet7(int capacity) {
        super(capacity);
    }

    protected int H(int value)
    {
        return (int)(_values.length * (value * A % 1));
    }
}
