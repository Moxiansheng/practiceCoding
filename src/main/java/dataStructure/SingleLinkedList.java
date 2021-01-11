package dataStructure;

public class SingleLinkedList <T> implements LinkedList <T>{
    private int length = 0;
    private SingleListNode<T> head = new SingleListNode<T>();
    private SingleListNode<T> temp = head;

    public SingleLinkedList(){

    }

    public SingleLinkedList(T val){
        addAtTail(val);
    }

    public SingleLinkedList(T[] vals){
        for(T val : vals){
            addAtTail(val);
        }
    }

    @Override
    public T getHead() {
        return get(0);
    }

    @Override
    public T getTail() {
        return get(length - 1);
    }

    @Override
    public T get(int index) {
        return indexValidityDel(index) ? getNode(index).getNext().getVal() : null;
    }

    private SingleListNode<T> getNode(int index){
        temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.getNext();
        }
        return temp;
    }

    @Override
    public T addAtHead(T val) {
        return addAtIndex(val, 0);
    }

    @Override
    public T addAtTail(T val) {
        return addAtIndex(val, length);
    }

    @Override
    public T addAtIndex(T val, int index) {
        if (indexValidityAdd(index)) {
            temp = getNode(index);
            if(temp != null){
                SingleListNode<T> node = new SingleListNode<T>(val);
                node.setNext(temp.getNext());
                temp.setNext(node);
                length++;
                return val;
            }
        }
        return null;
    }

    @Override
    public T delHead() { return delIndex(0); }

    @Override
    public T delTail() { return delIndex(length - 1); }

    @Override
    public T delIndex(int index) {
        T res = null;
        if(indexValidityDel(index)){
            temp = getNode(index);
            if(temp != null){
                res = temp.getNext().getVal();
                temp.setNext(temp.getNext().getNext());
                length--;
            }
        }
        return res;
    }

    @Override
    public boolean contains(T val) {
        temp = head;
        for (int i = 0; i < length; i++) {
            if(temp.getNext().getVal() == val) { return true;}
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public boolean indexValidityDel(int index){
        return index >= 0 && index < length;
    }

    @Override
    public boolean indexValidityAdd(int index) {
        return index >= 0 && index <= length;
    }

    @Override
    public int size() {
        return length;
    }
}
