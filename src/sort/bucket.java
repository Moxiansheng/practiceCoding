package sort;

import java.util.Arrays;

public class bucket extends base{
    int bucketSize = 1;
    int bucketSpan = 1;
    int[][] bucket = new int[bucketSize][0];
    base sort = null;

    public bucket(int[] array, int bucketSize){
        super(array);
        setBucket(bucketSize);
    }

    public bucket(int[] array, boolean asc, int bucketSize) {
        super(array, asc);
        setBucket(bucketSize);
    }

    @Override
    public void sort(){
        for(int num : array){
            int loc = (num - minVal) / bucketSpan;
            bucket[loc] = arrAppend(bucket[loc], num);
        }

        int loc = 0;
        for(int[] arr : bucket){
            sort = new bubble(arr);
            sort.sort();
            for(int num : sort.getArray()){
                array[loc++] = num;
            }
        }
        ascOrDesc();
    }

    public void setBucketSize(int bucketSize) {
        this.bucketSize = bucketSize;
    }

    public void setBucketSpan() {
        this.bucketSpan = (maxVal - minVal) / bucketSize + 1;
    }

    public void setBucket(int bucketSize) {
        setBucketSize(bucketSize);
        setBucketSpan();
        this.bucket = new int[bucketSize][0];
    }

    private int[] arrAppend(int[] arr, int value) {
        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = value;
        return arr;
    }
}
