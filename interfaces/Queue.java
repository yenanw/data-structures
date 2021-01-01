package interfaces;

public interface Queue<T> extends Collection<T> {
    /**
     * Adds the specified item to the queue
     * 
     * @param item The specified item
     */
    void enqueue(T item);

    /**
     * Removes the item at the head of the queue
     * 
     * @return The removed item
     */
    T dequeue();

    /**
     * Retrieves the item at the head of the queue
     * 
     * @return The item at the head of the queue
     */
    T peek();
}
