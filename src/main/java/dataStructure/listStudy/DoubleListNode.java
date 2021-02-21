package dataStructure.listStudy;

public class DoubleListNode<T> extends ListNode<T> {
    private DoubleListNode next = null;
    private DoubleListNode pre = null;

    public DoubleListNode(){

    }

    public DoubleListNode(T val){
        super(val);
        setPre(pre);
        setNext(next);
    }

    public DoubleListNode(DoubleListNode<T> next, DoubleListNode<T> pre){
        setPre(pre);
        setNext(next);
    }

    public DoubleListNode<T> getNext() {
        return next;
    }

    public DoubleListNode<T> getPre() {
        return pre;
    }

    public void setNext(DoubleListNode<T> next) {
        this.next = next;
    }

    public void setPre(DoubleListNode<T> pre) {
        this.pre = pre;
    }
}
