package dataStructure.queueStudy;

import dataStructure.listStudy.SingleLinkedList;

public class LinkedListQueue<T> {
    private SingleLinkedList<T> queue = new SingleLinkedList<>();

    public T offer(T val){
        queue.addAtTail(val);
        return val;
    }

    public T poll(){
        return queue.isEmpty() ? null : queue.delHead();
    }

    public T peek(){
        return queue.isEmpty() ? null : queue.getHead();
    }

    public int getLen(){
        return queue.size();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }
}
