package sort;

public class count extends base{
    int[] countArr = null;

    public count(int[] array){
        super(array);
    }

    public count(int[] array, boolean asc){
        super(array, asc);
        initCountArr();
    }

    @Override
    public void sort(){
        countNum();
        int loc = 0;
        for(int i = 0; i < countArr.length; ++i){
            while(countArr[i] > 0){
                array[loc++] = i + minVal;
                countArr[i]--;
            }
        }
    }

    public void initCountArr() {
        this.countArr = new int[maxVal - minVal + 1];
    }

    public void sortStability(){
        countNum();
        countArr[0]--;
        for(int i = 1; i < countArr.length; ++i){
            countArr[i] += countArr[i - 1];
        }
        int[] temp = new int[len];
        for(int num : array){
            temp[countArr[num - minVal]--] = num;
        }
        System.arraycopy(temp, 0, array, 0, len);
    }

    private void countNum(){
        for(int num : array){
            countArr[num - minVal]++;
        }
    }
}
