package hashCode;

public class singleIntSet {
    private Object[] _values = new Object[10];

    protected int H(int item){
        return item;
    }

    public void Add(int item){
        _values[H(item)] = item;
    }

    public void Remove(int item){
        _values[H(item)] = null;
    }

    public boolean Contains(int item){
        if (_values[H(item)] == null)
            return false;
        else
            return (int)_values[H(item)] == item;
    }
}
