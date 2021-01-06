package sort;

public class shell extends base{
    public shell(int[] array){
        super(array);
    }

    public shell(int[] array, boolean asc){
        super(array, asc);
    }

    @Override
    public void sort(){
        for(int step = len / 2; step >= 1; step /= 2){
            int temp = 0;
            for(int i = step; i < len; i++) {
                temp = array[i];
                int j = i - step;
                while (j >= 0 && array[j] > temp) {
                    array[j + step] = array[j];
                    j -= step;
                }
                array[j + step] = temp;
            }
        }
        ascOrDesc();
    }
}
