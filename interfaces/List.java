package interfaces;

public interface List<T> extends Collection<T> {
    /**
     * Adds the specified item to the end of the list
     * 
     * @param item The specified item
     */
    void add(T item);
    
    /**
     * Removes the item at the specified index
     * 
     * @param index The specified index
     */
    void remove(int index);

    /**
     * Retrieves the item at the specified index
     * 
     * @param index The specified index
     * @return The item at the specified index
     */
    T get(int index);

    /**
     * Replaces the item at the specified index with the new item
     * 
     * @param index The specified index
     * @param item  The new item
     * @return The previous item before replacement
     */
    T set(int index, T item);
}
