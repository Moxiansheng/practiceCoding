package hashCode;

public class DoubleIntSet extends DivPrimeIntSet {
    public DoubleIntSet(int capacity){
        super(capacity);
    }

    protected int H2(int val){
        // “1 +” 是为了避免H2出现返回值为0的情况
        // 因为那样的话，若H()出现碰撞，就会碰撞了
        return 1 + val % (_values.length - 1);
    }

    @Override
    protected int TH(int val){
        return (H(val) + val * H2(val)) % _values.length;
    }
}
