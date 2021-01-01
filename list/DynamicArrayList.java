package list;

import java.util.Iterator;

import interfaces.List;

@SuppressWarnings("unchecked")
public class DynamicArrayList<T> implements List<T> {
    private T[] arr;
    private int size;

    // if the size gets too big, we expand the array
    private static final double EXPAND_FACTOR = 2d;
    // if the size gets to small, we shrink the array down,
    // but you should never shrink the array as much as you expand it, as that
    // would worsen the average complexity to O(n) with certain input,
    // ----
    // for instances if the array just expanded and somehow the user decides to
    // run a sequence of actions like remove-add-remove-add...
    private static final double SHRINK_FACTOR = 0.75d;

    public DynamicArrayList() {
        this(8);
    }

    public DynamicArrayList(int init_capacity) {
        this.arr = (T[]) (new Object[init_capacity]);
        this.size = 0;
    }

    @Override
    public void add(T item) {
        // check first if the array is already full
        if (size >= arr.length)
            arr = resize(arr, EXPAND_FACTOR);
        // we add the item to the end and then increment the size
        arr[size++] = item;
    }

    @Override
    public void remove(int index) {
        // check first if the array is too small
        if (arr.length * SHRINK_FACTOR > size)
            arr = resize(arr, SHRINK_FACTOR);
        
        // remove the item at the specified index and shift everything right of
        // the given index 1 step to the left
        for (int i = index; i < size-1; i++) {
            arr[i] = arr[i+1];
        }
        // remove the previous last item and then decrement the size
        arr[size--] = null;
    }

    @Override
    public T get(int index) {
        // if the index is within bound
        if (index < size && index >= 0)
            return arr[index];
        // else throw an exception
        throw new IndexOutOfBoundsException();
    }

    @Override
    public T set(int index, T item) {
        // if the index is within bound
        if (index < size && index >= 0) {
            T prev = arr[index];
            // replace the item
            arr[index] = item;
            // return the previous item at this index
            return prev;
        }
        // else throw an exception
        throw new IndexOutOfBoundsException();
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
        return new ListIterator();
    }

    // simple iterator over the list, but it does not check for concurrent
    // modification, though sufficient for this simple project
    private class ListIterator implements Iterator<T>{
        private int pointer = 0;

        @Override
        public boolean hasNext() {
            return pointer < size;
        }

        @Override
        public T next() {
            return arr[pointer++];
        }
    }

    private T[] resize(T[] arr, double factor) {
        // always round up the new size to prevent various problems,
        // such as the old array's length being 0
        int nSize = (int) (arr.length * factor) + 1;
        // initialize the new array with a new size
        T[] nArr = (T[]) (new Object[nSize]);
        // copy all values from the old array into the new array
        for (int i = 0; i < arr.length; i++) {
            nArr[i] = arr[i];
        }
        return nArr;
    }
}
