package interfaces;

public interface Stack<T> extends Collection<T> {
    /**
     * Pushes an item onto the stack
     * 
     * @param item The specified item
     */
    void push(T item);

    /**
     * Removes an item from the top of the stack
     * 
     * @return The removed item
     */
    T pop();

    /**
     * Retreives the item from the top of the stack
     * 
     * @return The item on the top of the stack
     */
    T peek();
}
