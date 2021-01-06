package sort;

import java.util.Arrays;
import common.common;

public abstract class base {
    protected int minVal = 0;
    protected int maxVal = 0;
    protected int[] array = null;
    protected int len = 0;
    protected boolean asc = true;

    protected base(int[] array){
        setArray(array);
        setMinMax();
    }

    protected base(int[] array, boolean asc){
        setArray(array);
        setAsc(asc);
        setMinMax();
    }

    protected void ascOrDesc(){
        if(!asc){
            reverseArr();
        }
    }

    protected void reverseArr(){
        int[] temp = new int[len];
        System.arraycopy(array, 0, temp, 0, len);
        for(int i = 0; i < len; i++){
            array[len - 1 - i] = temp[i];
        }
    }

    protected void setMinMax(){
        minVal = min();
        maxVal = max();
    }

    public void setArray(int[] array) {
        this.array = Arrays.copyOf(array, array.length);
        this.len = this.array.length;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public abstract void sort();

    protected void swap(int a, int b){
        array = common.swap(array, a, b, 0, len);
    }

    protected void swap(int a, int b, int min, int max){
        array = common.swap(array, a, b, min, max);
    }

    public void show(){
        for(int i : array){
            System.out.println(i);
        }
    }

    protected int max(){
        int res = array[0];
        for(int num : array){
            if(num > res) res = num;
        }
        return res;
    }

    protected int min(){
        int res = array[0];
        for(int num : array){
            if(num < res) res = num;
        }
        return res;
    }

    public int[] getArray(){
        return array;
    }
}
