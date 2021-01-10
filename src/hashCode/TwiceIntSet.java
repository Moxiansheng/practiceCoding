package hashCode;

public class TwiceIntSet extends LineIntSet {
    private static int c1 = 1;
    private static int c2 = 1;

    @Override
    protected int LH(int item, int i){
        return (H(item) + c1 * i + c2 * i * i) % 10;
    }
}
