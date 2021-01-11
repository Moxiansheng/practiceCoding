package sort;

public class bubble extends base{
    public bubble(int[] array){
        super(array);
    }

    public bubble(int[] array, boolean asc){
        super(array, asc);
    }

    @Override
    public void sort(){
        for(int i = 1; i < len; ++i){
            for(int j = 0; j < len - i; ++j){
                if(array[j] > array[j + 1]){
                    swap(j, j + 1);
                }
            }
        }
        ascOrDesc();
    }
}
