package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

import interfaces.Deque;

@SuppressWarnings("unchecked")
public class DynamicArrayDeque<T> implements Deque<T> {
    private T[] deque;
    private int size;
    private int head;
    private int tail;

    // identical to DynamicArrayList
    private static final double EXPAND_FACTOR = 2d;
    private static final double SHRINK_FACTOR = 0.5d;
    private static final double SHRINK_THRESHOLD = 0.25d;

    public DynamicArrayDeque() {
        this(8);
    }

    public DynamicArrayDeque(int init_capacity) {
        this.deque = (T[]) (new Object[init_capacity]);
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    @Override
    public void addFirst(T item) {
        // special case when the deque is empty, moves the tail instead of head
        if (isEmpty()) {
            deque[tail++] = item;
            size++;
            return;
        }

        if (head == tail && size() > 1)
            deque = resize(deque, EXPAND_FACTOR);
        // move the head first, then set the item
        head = Math.floorMod(head-1, deque.length);
        deque[head] = item;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (head == tail && !isEmpty())
            deque = resize(deque, EXPAND_FACTOR);
        // set the item first, then move the tail
        deque[tail] = item;
        tail = (tail + 1) % deque.length;
        size++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        
        if (deque.length * SHRINK_THRESHOLD > size)
            deque = resize(deque, SHRINK_FACTOR);
        // the head is always pointing to the actual element
        T value = deque[head];
        deque[head] = null;
        head = (head + 1) % deque.length;
        size--;
        return value;
    }

    @Override
    public T removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        
        if (deque.length * SHRINK_THRESHOLD > size)
            deque = resize(deque, SHRINK_FACTOR);
        // the tail is always pointing to where the element would be next
        T value = deque[tail-1];
        deque[tail-1] = null;
        // floorMod due to modulos doesn't work well with negative numbers
        tail = Math.floorMod(tail-1, deque.length);
        size--;
        return value;
    }

    @Override
    public T getFirst() {
        if (isEmpty())
            return null;

        return deque[head];
    }

    @Override
    public T getLast() {
        if (isEmpty())
            return null;

        return deque[tail-1];
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
            int i = ((index++) + head) % deque.length;
            return deque[i];
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
        // deque (queue) in mind, so we copy from the head position instead of
        // index 0
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
