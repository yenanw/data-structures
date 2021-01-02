package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

import interfaces.Deque;

public class LinkedListDeque<T> implements Deque<T> {
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

    public LinkedListDeque() {
        head = tail = null;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        // special case when the list is empty
        if (isEmpty()) {
            Node nNode = new Node(item, null, null);
            head = nNode;
            tail = nNode;
            size++;
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
        if (isEmpty()) {
            Node nNode = new Node(item, null, null);
            head = nNode;
            tail = nNode;
            size++;
            return;
        }

        Node nNode = new Node(item, tail, null);
        tail.next = nNode;
        tail = nNode;
        size++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty())
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
        if (isEmpty())
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
