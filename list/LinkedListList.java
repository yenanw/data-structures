package list;

import java.util.Iterator;
import java.util.NoSuchElementException;

import interfaces.List;
import interfaces.Deque;

public class LinkedListList<T> implements List<T>, Deque<T> {
    private Node head;
    private Node tail;
    private int size;

    private class Node {
        T value;
        Node prev;
        Node next;
        
        Node(T value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    public LinkedListList() {
        head = tail = null;
        size = 0;
    }

    @Override
    public void add(T item) {
        addLast(item);
    }

    @Override
    public void remove(int index) {
        // check first if the index is out of bounds
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException();

        // pass on the instruction if possible
        if (index == 0)
            removeFirst();
        else if (index == size-1)
            removeLast();
        else {
            // the nodes found here are guaranteed to have both prev and next
            // nodes, since they can never be the first or the last node
            Node node = head.next;
            for (int i = 1; i < size-1; i++) {
                if (i == index) {
                    Node prev = node.prev;
                    Node next = node.next;
                    prev.next = next;
                    next.prev = prev;
                    // ideally, you should discard the node to make sure that
                    // it's deleted, but here we will just leave it to java's
                    // garbage collectors
                    size--;
                    break;
                }
                // while not at the goal, keep traversing through the list
                node = node.next;
            }
        }
    }

    @Override
    public T get(int index) {
        // check first if the index is out of bounds
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException();
        
        Node node = head;
        T value = null;
        for (int i = 0; i < size; i++) {
            if (i == index) {
                value = node.value;
                break;
            }
            // while not at the goal, keep traversing through the list
            node = node.next;
        }
        // return the value
        return value;
    }

    @Override
    public T set(int index, T item) {
        // check first if the index is out of bounds
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException();

        Node node = head;
        T prev_value = null;
        for (int i = 0; i < size; i++) {
            if (i == index) {
                prev_value = node.value;
                node.value = item;
                break;
            }
            // while not at the goal, keep traversing through the list
            node = node.next;
        }
        // return the value previously at the index
        return prev_value;
    }
    
    @Override
    public void addFirst(T item) {
        // special case when the list is empty
        if (size() == 0) {
            Node nNode = new Node(item, null, null);
            head = nNode;
            tail = nNode;
            return;
        }

        Node nNode = new Node(item, null, head);
        head.prev = nNode;
        head = nNode;
        size++;
    }

    @Override
    public void addLast(T item) {
        // special case when the list is empty
        if (size() == 0) {
            Node nNode = new Node(item, null, null);
            head = nNode;
            tail = nNode;
            return;
        }

        Node nNode = new Node(item, tail, null);
        tail.next = nNode;
        tail = nNode;
        size++;
    }

    @Override
    public T removeFirst() {
        if (size() == 0)
            throw new NoSuchElementException();

        Node first = head;
        head = head.next;
        // special case when the size is 1
        if (head == null)
            tail = null;
        else 
            head.prev = null;
        size--;
        return first.value;
    }

    @Override
    public T removeLast() {
        if (size() == 0)
            throw new NoSuchElementException();
        
        Node last = tail;
        tail = tail.prev;
        // special case when the size is 1
        if (tail == null)
            head = null;
        else
            tail.next = null;
        size--;
        return last.value;
    }

    @Override
    public T getFirst() {
        return head.value;
    }

    @Override
    public T getLast() {
        return tail.value;
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
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T value = current.value;
            current = current.next;
            return value;
        }
    }
}
