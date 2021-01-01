package interfaces;

public interface Set<T> extends Collection<T> {
    /**
     * Adds the specified item to the set, note that the size of the set will
     * not change even if the specified item already exists in the set
     * 
     * @param item The item to be added
     */
    void add(T item);

    /**
     * Removes the specified item from the set, does nothing if the specified
     * item does not exist in the set
     * 
     * @param item The item to be removed
     */
    void remove(T item);

    /**
     * Checks whether or not the specified item exists in the set
     * 
     * @param item The specified item
     * @return true if the specified item already exists in the set, else false
     */
    boolean contains(T item);
}
