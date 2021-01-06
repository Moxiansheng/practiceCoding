package sort;

import java.util.ArrayList;

public class baseSort extends base{
    int ten = 10;
    ArrayList<Integer>[] baseArrPos = new ArrayList[ten];
    ArrayList<Integer>[] baseArrNeg = new ArrayList[ten];
    int absMax = 0;
    int times = 0;
    int coe = 1;

    public baseSort(int[] array){
        super(array);
    }

    public baseSort(int[] array, boolean asc){
        super(array, asc);
    }

    @Override
    public void sort(){
        init();
        setAbsMax();
        while(absMax > 0){
            absMax /= ten;
            times++;
        }
        while(times > 0){
            for(int i = 0; i < len; i++){
                int num = array[i];
                num /= coe;
                if(num >= 0){
                    baseArrPos[num % ten].add(array[i]);
                }else{
                    baseArrNeg[Math.abs(num) % ten].add(array[i]);
                }
            }
            fillPos(fillNeg(0));
            coe *= ten;
            times--;
        }
        ascOrDesc();
    }

    private void init(){
        for(int i = 0; i < ten; i++){
            baseArrPos[i] = new ArrayList<Integer>();
            baseArrNeg[i] = new ArrayList<Integer>();
        }
    }

    private void setAbsMax(){
        absMax = Math.max(Math.abs(max()), Math.abs(min()));
    }

    private int fillPos(int loc){
        for(int i = 0; i < ten; ++i){
            while(!baseArrPos[i].isEmpty()){
                array[loc++] = baseArrPos[i].remove(0);
            }
        }
        return loc;
    }

    private int fillNeg(int loc){
        for(int i = ten - 1; i >= 0; --i){
            while(!baseArrNeg[i].isEmpty()){
                array[loc++] = baseArrNeg[i].remove(0);
            }
        }
        return loc;
    }
}
