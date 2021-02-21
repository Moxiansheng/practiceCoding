package dataStructure.heapStudy;

import java.util.Arrays;
import common.common;

public class heap {
    static int ZERO = 0;
    static int EXPAND = 2;

    int size = 32;
    int[] array = new int[size];
    boolean asc = true; // true: minHeap

    int length = 0;

    final static String warning_no_val = "The heap doesn't contain :";
    final static String warning_empty = "The heap is empty!";

    public heap(){

    }

    public heap(int[] arr){
        buildHeap(arr);
    }

    public heap(int[] arr, boolean asc){
        setAsc(asc);
        buildHeap(arr);
    }

    public void buildHeap(int[] arr){
        for(int num : arr){
            insert(num);
        }
    }

    public void insert(int val){
        if(is_full()){ self_expand(); }
        array[length++] = val;
        shift_up(length - 1);
    }

    public int remove(){
        int temp = array[ZERO];
        removeByIndex(ZERO);
        return temp;
    }

    public void self_expand(){
        size *= EXPAND;
        int[] temp = new int[size];
        System.arraycopy(array, ZERO, temp, ZERO, length);
        array = Arrays.copyOf(temp, size);
    }

    public boolean is_full(){
        return length >= size;
    }

    public boolean is_empty(){
        return length <= ZERO;
    }

    private void shift_up(int loc){
        if(is_empty() || loc < 1) return;
        int par = (loc - 1) >> 1;
        if(asc == array[par] > array[loc]){
            array = common.swap(array, par, loc, ZERO, length - 1);
            shift_up(par);
        }
    }

    private void shift_down(int loc){
        if(is_empty() || loc >= length) return;
        int l = (loc << 1) + 1;
        int r = loc + 1 << 1;
        int m = asc ^ array[l] > array[r] ? l : r;
        if(asc == array[loc] > array[m]){
            array = common.swap(array, m, loc, ZERO, length - 1);
            shift_down(m);
        }
    }

    public void show(){
        System.out.println();
        if(is_empty()){
            System.out.println(warning_empty);
        }else{
            int l = ZERO, r = ZERO, pow = EXPAND;
            for(int i = ZERO; i < length; i++){
                if(i >= l && i <= r){
                    System.out.print(array[i] + " ");
                }else{
                    l = r + 1;
                    r += pow;
                    pow *= EXPAND;
                    System.out.println();
                    i--;
                }
            }
        }
        System.out.println();
    }

    public void removeByVal(int val){
        removeByIndex(search(val));
    }

    public void removeByIndex(int index){
        if(!is_empty() && index > -1){
            replaceByIndex(index, array[--length]);
        }else{
            System.out.println();
            System.out.println(warning_empty);
            System.out.println();
        }
    }

    public void replaceByVal(int srcVal, int destVal){
        replaceByIndex(search(srcVal), destVal);
    }

    public void replaceByIndex(int srcIndex, int destVal){
        if(srcIndex == -1){
            System.out.println();
            System.out.println(warning_no_val + srcIndex);
            System.out.println();
        }else {
            array[srcIndex] = destVal;
            shift_down(srcIndex);
            shift_up(srcIndex);
        }
    }

    public boolean contains(int val){
        return search(val) == -1;
    }

    public int search(int val){
        for(int i = 0; i < length; ++i){
            if(array[i] == val){
                return i;
            }
        }
        return -1;
    }

    public int getLength(){
        return length;
    }

    public int getSize() {
        return size;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
