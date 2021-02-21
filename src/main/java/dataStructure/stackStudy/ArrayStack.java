package dataStructure.stackStudy;

import java.util.Arrays;

public class ArrayStack<T> {
    private int size = 16;
    private Object[] stack;
    private int tail = 0;
    private final int GROW = 2;

    public ArrayStack(){
        stack = new Object[size];
    }

    public ArrayStack(int cap){
        this.size = cap;
        stack = new Object[size];
    }

    public void push(T value) {
        if (isFull()) {
            size = size * 2;
            stack = Arrays.copyOf(stack, size);
        }
        stack[tail++] = value;
    }

    public T pop() {
        return isEmpty() ? null : (T)stack[tail-- - 1];
    }

    public T peek(){
        return isEmpty() ? null : (T)stack[tail - 1];
    }

    public boolean isEmpty(){
        return tail == 0;
    }

    public boolean isFull(){
        return tail == size;
    }

    public int size() {
        return tail;
    }
}
