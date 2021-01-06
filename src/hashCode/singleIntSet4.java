package hashCode;

import java.util.LinkedList;

public class singleIntSet4 extends singleIntSet3{
    public void Add(int item) {
        if (_values[H(item)] == null) {
            LinkedList<Integer> ls = new LinkedList<Integer>();
            ls.add(item);
            _values[H(item)] = ls;
        } else {
            LinkedList<Integer> ls = (LinkedList<Integer>) _values[H(item)];
            ls.add(item);
        }
    }

    public void Remove(int item) {
        LinkedList<Integer> ls = (LinkedList<Integer>) _values[H(item)];
        ls.remove(item);
    }

    public boolean Contains(int item) {
        if (_values[H(item)] == null) {
            return false;
        } else {
            LinkedList<Integer> ls = (LinkedList<Integer>) _values[H(item)];
            return ls.contains(item);
        }
    }
}
