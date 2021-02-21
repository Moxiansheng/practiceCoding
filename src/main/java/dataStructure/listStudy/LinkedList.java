package dataStructure.listStudy;

public interface LinkedList<T>{
    T getHead();

    T getTail();

    T get(int index);

    T addAtHead(T val);

    T addAtTail(T val);

    T addAtIndex(T val, int index);

    T delHead();

    T delTail();

    T delIndex(int index);

    boolean contains(T val);

    boolean isEmpty();

    boolean indexValidityDel(int index);

    boolean indexValidityAdd(int index);

    int size();
}
