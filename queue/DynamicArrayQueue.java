package queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import interfaces.Queue;

@SuppressWarnings("unchecked")
public class DynamicArrayQueue<T> implements Queue<T> {
    private T[] queue;
    private int size;
    private int head;
    private int tail;

    // identical to DynamicArrayList
    private static final double EXPAND_FACTOR = 2d;
    private static final double SHRINK_FACTOR = 0.5d;
    private static final double SHRINK_THRESHOLD = 0.25d;

    public DynamicArrayQueue() {
        this(8);
    }

    public DynamicArrayQueue(int init_capacity) {
        this.queue = (T[]) (new Object[init_capacity]);
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    @Override
    public void enqueue(T item) {
        if (head == tail && !isEmpty())
            queue = resize(queue, EXPAND_FACTOR);

        queue[tail] = item;
        tail = (tail + 1) % queue.length;
        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        
        if (queue.length * SHRINK_THRESHOLD > size)
            queue = resize(queue, SHRINK_FACTOR);

        T value = queue[head];
        queue[head] = null;
        head = (head + 1) % queue.length;
        size--;
        return value;
    }

    @Override
    public T peek() {
        if (isEmpty())
            return null;

        return queue[head];
    }
    
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new CircularArrayIterator();
    }

    private class CircularArrayIterator implements Iterator<T> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            int i = ((index++) + head) % queue.length;
            return queue[i];
        }
        
    }

    private T[] resize(T[] arr, double factor) {
        // always round up the new size to prevent various problems,
        // such as the old array's length being 0
        int nSize = (int) (arr.length * factor) + 1;
        // initialize the new array with a new size
        T[] nArr = (T[]) (new Object[nSize]);
        // always take the smallest length to make sure the index is in bound
        int length = Math.min(nArr.length, arr.length);
        // copy all values from the old array into the new array, but with
        // queue in mind, so we copy from the head position instead of index 0
        for (int i = 0; i < length; i++) {
            int index = (head + i) % arr.length;
            nArr[i] = arr[index];
        }
        // adjust the new head and tail
        head = 0;
        tail = size();
        return nArr;
    }
}
