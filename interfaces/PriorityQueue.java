package interfaces;

public interface PriorityQueue<T> extends Collection<T> {
    /**
     * Adds the specified item to the queue
     * 
     * @param item The specified item
     */
    void add(T item);

    /**
     * Depending on the implementation, remove either the minimum or the
     * maximum element
     * 
     * @return The removed item
     */
    T poll();

    /**
     * Depending on the implementation, retrieves either the minimum or the
     * maximum element
     * 
     * @return The min/max item
     */
    T peek();
}
