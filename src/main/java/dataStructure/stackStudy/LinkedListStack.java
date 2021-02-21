package dataStructure.stackStudy;

import dataStructure.listStudy.SingleLinkedList;

public class LinkedListStack<T> {
    private SingleLinkedList<T> stack = new SingleLinkedList<>();

    public LinkedListStack(){
    }

    public LinkedListStack(T val){
        push(val);
    }

    public void push(T value) {
        stack.addAtTail(value);
    }

    public T pop() {
        return isEmpty() ? null : stack.delTail();
    }

    public T peek(){
        return isEmpty() ? null : stack.getTail();
    }

    public boolean isEmpty(){
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }
}
