package sort;

import dataStructure.heapStudy.heap;

public class heapSort extends base{
    heap h = new heap();

    public heapSort(int[] array){
        super(array);
    }

    public heapSort(int[] array, boolean asc){
        super(array, asc);
    }

    @Override
    public void sort(){
        h = new heap(array, asc);
        for(int i = 0; i < len; i++){
            array[i] = h.remove();
        }
    }
}
