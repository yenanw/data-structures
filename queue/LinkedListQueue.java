package queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import interfaces.Queue;

public class LinkedListQueue<T> implements Queue<T> {
    private Node head;
    private Node tail;
    private int size;

    private class Node {
        T value;
        Node next;
        
        Node(T value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    public LinkedListQueue() {
        head = tail = null;
        size = 0;
    }

    @Override
    public void enqueue(T item) {
        // special case when the list is empty
        if (isEmpty()) {
            Node nNode = new Node(item,  null);
            head = nNode;
            tail = nNode;
            size++;
            return;
        }

        Node nNode = new Node(item, null);
        tail.next = nNode;
        tail = nNode;
        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();

        Node first = head;
        head = head.next;
        // special case when the queue becomes empty
        if (head == null)
            tail = null;
        size--;
        return first.value;
    }

    @Override
    public T peek() {
        if (isEmpty())
            return null;

        return head.value;
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
