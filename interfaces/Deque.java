package interfaces;

public interface Deque<T> extends Collection<T> {
    /**
     * Adds the specified item at the head of the deque
     * 
     * @param item The specified item
     */
    void addFirst(T item);

    /**
     * Adds the specified item at the tail of the deque
     * 
     * @param item The specified item
     */
    void addLast(T item);

    /**
     * Removes the head of the deque
     * 
     * @return The head
     */
    T removeFirst();

    /**
     * Removes the tail of the deque
     * 
     * @return The tail
     */
    T removeLast();

    /**
     * Retrieves the head of the deque
     * 
     * @return The head
     */
    T getFirst();

    /**
     * Retrieves the tail of the deque
     * 
     * @return The tail
     */
    T getLast();
}
