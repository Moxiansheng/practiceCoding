package hashCode;

public class singleIntSet {
    /**
     * [0, 10)
     */
    protected Object[] _values = new Object[10];

    protected int H(int item){
        return item;
    }

    protected int TH(int item) { return H(item); }

    public void Add(int item){
        _values[TH(item)] = item;
    }

    public void Remove(int item){
        _values[TH(item)] = null;
    }

    public boolean Contains(int item){
        if (_values[TH(item)] == null)
            return false;
        else
            return (int)_values[TH(item)] == item;
    }
}
