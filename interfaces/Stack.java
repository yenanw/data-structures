package interfaces;

public interface Stack<T> extends Collection<T> {
    /**
     * Pushes an item onto the stack
     */
    void push();

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
