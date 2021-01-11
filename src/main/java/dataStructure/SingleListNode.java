package dataStructure;

public class SingleListNode<T> extends ListNode <T>{
    private SingleListNode next = null;

    public SingleListNode(){

    }

    public SingleListNode(T val){
        super(val);
    }

    public SingleListNode (SingleListNode<T> next){
        setNext(next);
    }

    public SingleListNode(T val, SingleListNode<T> next){
        super(val);
        setNext(next);
    }

    public SingleListNode<T> getNext() {
        return next;
    }

    public void setNext(SingleListNode<T> next) {
        this.next = next;
    }
}
