package hashCode;

public class LineIntSet extends singleIntSet{
    @Override
    protected int H(int item){
        return item % 10;
    }

    protected int LH(int item, int i){
        return (H(item) + i) % 10;
    }

    @Override
    public void Add(int item){
        int times = 0, loc;
        do{
            loc = LH(item, times);
            if(_values[loc] == null || (int)_values[loc] == -1){
                _values[loc] = item;
                return;
            }else{
                times += 1;
            }
        }while (times <= 10);
        System.out.println("集合溢出");
    }

    @Override
    public void Remove(int item){

        int times = 0, loc;
        do{
            loc = LH(item, times);
            if(_values[loc] == null){
                return;
            }
            if((int)_values[loc] == item){
                _values[loc] = -1;
                return;
            }else{
                times++;
            }
        }while (times <= 10);
    }

    @Override
    public boolean Contains(int item){
        int i = 0; // 已经探查过的槽的数量
        int j = 0; // 想要探查的地址
        do {
            j = LH(item, i);
            if (_values[j] == null)
                return false;
            if ((int)_values[j] == item){
                return true;
            } else{
                i += 1;
            }
        } while (i <= 10);
        return false;
    }
}
