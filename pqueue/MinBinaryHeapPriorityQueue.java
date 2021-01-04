package pqueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import interfaces.PriorityQueue;
import list.DynamicArrayList;

public class MinBinaryHeapPriorityQueue<T extends Comparable<T>>
    implements PriorityQueue<T>
{
    // dynamic array representing the heap, can also be a static array
    // if you want to handle the resizing part yourself, but this is simpler
    private DynamicArrayList<T> heap;
   
    public MinBinaryHeapPriorityQueue() {
        this(9);
    }

    public MinBinaryHeapPriorityQueue(int init_capacity) {
        // following the convention of 1-based indexing
        heap = new DynamicArrayList<>(init_capacity+1);
        // occupy the 0th element since we are not gonna be using it
        heap.add(null);
    }

    @Override
    public void add(T item) {
        // always add the new item at the bottom, so that we can keep swapping
        // against its parent until the heap property preserves
        heap.add(item);

        int child = size();
        // element at index i/2 is the parent of element at index i
        int parent = child / 2;
        // while the child is not the minimum and the parent is bigger than
        // the child, the invariant breaks
        while (child > 1 && greater(parent, child)) {
            // so we swap the child and its parent
            swap(parent, child);
            
            child = parent;
            parent = child / 2;
        }
    }

    @Override
    public T poll() {
        // save the minimum to return later
        T min = peek();
        // the bottom right element is the last element in the array
        // representation of heap
        int bottomRight = size();
        // first step, swap the minimum with the bottom right element
        swap(1, bottomRight);
        // and then remove the bottom right element
        heap.remove(bottomRight);

        // we always start the the top of the heap when removing the
        // smallest element
        int parent = 1;
        // the left child is parent*2, the right child is parent*2+1
        int child = parent * 2;

        while (child <= size()) {
            if (child < size() && greater(child, child+1))
                // if the left child is bigger than the right child,
                // switch the child to the right child
                child++;

            if (!greater(parent, child))
                // if the parent if smaller than the child, then the
                // heap property is preserved, so just move on
                break;
            // else swap the child and the parent
            swap(parent, child);

            parent = child;
            child = parent * 2;
        }
        // return the previous minimum
        return min;
    }

    // to make a max binary heap, simply change > to < in this method,
    // even better to allow this class to use a custom comparator
    private boolean greater(int i, int j) {
        // assuming both i and j are in bounds, returns true if heap.get(i)
        // is greater than heap.get(j)
        return heap.get(i).compareTo(heap.get(j)) > 0;
    }

    private void swap(int i, int j) {
        // assuming both i and j are in bounds
        T t1 = heap.get(i);
        T t2 = heap.get(j);
        heap.set(i, t2);
        heap.set(j, t1);
    }

    @Override
    public T peek() {
        // the smallest element is always the first element
        return heap.get(1);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        // since this 1-based index, the 0th element is unused,
        // note that also returns the index of the last element in the
        // heap array
        return heap.size() - 1;
    }

    @Override
    public Iterator<T> iterator() {
        return new UnorderedArrayIterator();
    }

    // this iterator does not return the elements in any particular order
    private class UnorderedArrayIterator implements Iterator<T> {
        int pointer = 1;

        @Override
        public boolean hasNext() {
            return pointer <= size();
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            
            return heap.get(pointer++);
        }
    }
}
