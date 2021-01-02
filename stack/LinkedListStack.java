package stack;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import interfaces.Stack;

public class LinkedListStack<T> implements Stack<T> {
    private Node head;
    private int size;
    
    private class Node {
        T value;
        Node next;
        Node(T value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    public LinkedListStack() {
        head = null;
        size = 0;
    }

    @Override
    public void push(T item) {
        // works even if the stack is empty
        Node next = head;
        head = new Node(item, next);
        size++;
    }

    @Override
    public T pop() {
        // or return null, whatever satisfies your needs as long as it
        // takes care of empty stack
        if (isEmpty())    
            throw new EmptyStackException();
        
        Node prev = head;
        head = head.next;
        size--;
        return prev.value;
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
