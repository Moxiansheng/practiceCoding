package sort;

import java.util.Arrays;

public class merge extends base{
    public merge(int[] array) { super(array); }

    public merge(int[] array, boolean asc){
        super(array, asc);
    }

    @Override
    public void sort(){
        merge_sort(0, len - 1);
        ascOrDesc();
    }

    public void merge_sort(int l, int r){
        if (l == r) { return; }
        int m = l + ((r - l) >> 1);

        merge_sort(l, m);
        merge_sort(m + 1, r);
        merge(l, m, r);
    }

    public void merge(int l, int m, int r){
        int[] res = new int[r - l + 1];
        int loc = 0, f = l, s = m + 1;
        while (f <= m && s <= r) {
            res[loc++] = array[f] >= array[s] ? array[s++] : array[f++];
        }

        while (f <= m) {
            res[loc++] = array[f++];
        }

        while (s <= r) {
            res[loc++] = array[s++];
        }

        for(int num : res){ array[l++] = num; }
    }

}
