package hashCode;

public class MultIntSet extends DivPrimeIntSet {
    /**
     * 乘法散列表
     */
    private double A = Integer.MAX_VALUE / Math.pow(2, 32);
    public MultIntSet(int capacity) {
        super(capacity);
    }

    protected int H(int value)
    {
        return (int)(_values.length * (value * A % 1));
    }
}
