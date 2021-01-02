package stack;

import java.util.Iterator;
import java.util.NoSuchElementException;

import interfaces.Stack;

@SuppressWarnings("unchecked")
public class DynamicArrayStack<T> implements Stack<T> {
    private T[] stack;
    private int size;

    // identical to DynamicArrayList
    private static final double EXPAND_FACTOR = 2d;
    private static final double SHRINK_FACTOR = 0.5d;
    private static final double SHRINK_THRESHOLD = 0.25d;

    public DynamicArrayStack() {
        this(8);
    }

    public DynamicArrayStack(int init_capacity) {
        this.stack = (T[]) (new Object[init_capacity]);
        this.size = 0;
    }
    
    @Override
    public void push(T item) {
        if (size >= stack.length)
            stack = resize(stack, EXPAND_FACTOR);

        stack[size++] = item;
    }

    @Override
    public T pop() {
        if (stack.length * SHRINK_THRESHOLD > size)
            stack = resize(stack, SHRINK_FACTOR);

        T value = stack[--size];
        stack[size] = null;
        return value;
    }

    @Override
    public T peek() {
        return stack[size-1];
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
        return new ReverseArrayIterator();
    }

    private class ReverseArrayIterator implements Iterator<T> {
        int rPointer = size - 1;

        @Override
        public boolean hasNext() {
            return rPointer >= 0;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return stack[rPointer--];
        }
    }

    private T[] resize(T[] arr, double factor) {
        // always round up the new size to prevent various problems,
        // such as the old array's length being 0
        int nSize = (int) (arr.length * factor) + 1;
        // initialize the new array with a new size
        T[] nArr = (T[]) (new Object[nSize]);
        // always take the smallest length to make sure the index is in bound
        int size = Math.min(nArr.length, arr.length);
        // copy all values from the old array into the new array
        for (int i = 0; i < size; i++) {
            nArr[i] = arr[i];
        }
        return nArr;
    }
}
