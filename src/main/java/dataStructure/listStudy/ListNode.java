package dataStructure.listStudy;

public class ListNode <T>{
    private T val;

    public ListNode(){

    }

    public ListNode(T val){
        setVal(val);
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }
}
