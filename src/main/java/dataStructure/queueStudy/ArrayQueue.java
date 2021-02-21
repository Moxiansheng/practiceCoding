package dataStructure.queueStudy;

public class ArrayQueue<T> {
    private int size = 8;
    private Object[] queue;

    private int head = 0;
    private int tail = 0;
    private int len = 0;

    public ArrayQueue(){
        queue = new Object[size];
    }

    public ArrayQueue(int size) {
        this.size = size;
        queue = new Object[size];
    }

    public T offer(T val){
        if (!isFull()) {
            queue[tail] = val;
            tail = (tail + 1) % size;
            len++;
            return val;
        }
        return null;
    }

    public T poll(){
        T res = null;
        if (!isEmpty()) {
            res = (T) queue[head];
            head = (head + 1) % size;
            len--;
        }
        return res;
    }

    public T peek(){
        return !isEmpty() ? (T) queue[head] : null;
    }

    public boolean isFull(){
        return len == size;
    }

    public boolean isEmpty(){
        return len == 0;
    }

    public int getLen() {
        return len;
    }
}
