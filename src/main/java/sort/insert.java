package sort;

public class insert extends base{
    public insert(int[] array){
        super(array);
    }

    public insert(int[] array, boolean asc){
        super(array, asc);
    }

    @Override
    public void sort(){
        for(int i = 1; i < len; ++i){
            int j = i - 1, temp = array[i];
            while(j >= 0 && temp < array[j]){
                array[j + 1] = array[j--];
            }
            if(j != i - 1){
                array[j + 1] = temp;
            }
        }
        ascOrDesc();
    }
}
