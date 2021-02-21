package dataStructure.listStudy;

public class DoubleLinkedList<T> implements LinkedList<T> {
    private int length = 0;
    private DoubleListNode<T> head = new DoubleListNode<>();
    private DoubleListNode<T> tail = new DoubleListNode<>();
    private DoubleListNode<T> temp = head;

    public DoubleLinkedList(){
        initHeadTail();
    }

    public DoubleLinkedList(T val){
        initHeadTail();
        addAtTail(val);
    }

    public DoubleLinkedList(T[] vals){
        initHeadTail();
        for(T val : vals){
            addAtTail(val);
        }
    }

    private void initHeadTail(){
        head.setNext(tail);
        tail.setPre(head);
    }

    public T getHead() {
        return get(0);
    }

    public T getTail() {
        return get(length - 1);
    }

    public T get(int index) {
        return indexValidityDel(index) ? getNode(index).getNext().getVal() : null;
    }

    private DoubleListNode<T> getNode(int index){
        temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.getNext();
        }
        return temp;
    }

    public T addAtHead(T val) {
        return addAtIndex(val, 0);
    }

    public T addAtTail(T val) {
        return addAtIndex(val, length);
    }

    public T addAtIndex(T val, int index) {
        if (indexValidityAdd(index)) {
            temp = getNode(index);
            if(temp != null){
                DoubleListNode<T> node = new DoubleListNode<>(val);
                temp.getNext().setPre(node);
                node.setNext(temp.getNext());
                node.setPre(temp);
                temp.setNext(node);
                length++;
                return val;
            }
        }
        return null;
    }

    public T delHead() {
        return delIndex(0);
    }

    public T delTail() {
        return delIndex(length - 1);
    }

    public T delIndex(int index) {
        T res = null;
        if(indexValidityDel(index)){
            temp = getNode(index);
            if(temp != null){
                res = temp.getNext().getVal();
                temp.getNext().getNext().setPre(temp);
                temp.setNext(temp.getNext().getNext());
                length--;
            }
        }
        return res;
    }

    public boolean contains(T val) {
        temp = head;
        for (int i = 0; i < length; i++) {
            if(temp.getNext().getVal() == val) { return true;}
        }
        return false;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public boolean indexValidityDel(int index) {
        return index >= 0 && index < length;
    }

    public boolean indexValidityAdd(int index) {
        return index >= 0 && index <= length;
    }

    public int size() {
        return length;
    }
}
