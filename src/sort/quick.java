package sort;

public class quick extends base{
    public quick(int[] array){
        super(array);
    }

    public quick(int[] array, boolean asc){
        super(array, asc);
    }

    @Override
    public void sort(){
        quick_sort(0, len - 1);
        ascOrDesc();
    }

    public void quick_sort(int l, int r){
        if(l >= r || l < 0 || r >= len) return;
        int pivot = l + ((r - l) >> 1);
        swap(pivot, r, l, r);
        int s = l, b = r - 1;
        for(int i = l; i <= b; i++){
            if(array[i] < array[r]){
                swap(i, s++, l, r);
            }else{
                swap(i--, b--, l, r);
            }
        }
        swap(s, r, l, r);
        quick_sort(l, s - 1);
        quick_sort(s + 1, r);
    }

    public void partition(){

    }
}
