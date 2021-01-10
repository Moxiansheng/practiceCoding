package hashCode;

public class singleIntSet3 extends singleIntSet {
    /**
     * [0, 20) 存在碰撞
     */
    protected int H(int value) {
        return value <= 9 ? value : value - 10;
    }
}
