package sort;

public class select extends base{
    public select(int[] array){
        super(array);
    }

    public select(int[] array, boolean asc){
        super(array, asc);
    }

    @Override
    public void sort(){
        for(int i = 0; i < len - 1; ++i){
            int min = i;
            for(int j = i + 1; j < len; ++j){
                if(array[j] < array[min]){
                    min = j;
                }
            }
            if(i != min){
                swap(i, min);
            }
        }
        ascOrDesc();
    }
}
